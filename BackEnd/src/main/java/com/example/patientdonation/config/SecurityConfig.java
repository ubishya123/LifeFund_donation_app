package com.example.patientdonation.config;

import com.example.patientdonation.entity.User;
import com.example.patientdonation.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection as it's not needed for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // --- PUBLIC GET REQUESTS ---
                        // Allow anyone to view patients, fundraisers, comments, and uploaded reports
                        .requestMatchers(HttpMethod.GET, "/api/patients/**", "/uploads/**", "/api/patients/{patientId}/comments").permitAll()

                        // --- PUBLIC POST REQUESTS ---
                        // Allow anyone to register or initiate a donation
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/donation/create-order/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/donation/verify").permitAll()

                        // --- AUTHENTICATED REQUESTS ---
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Add the custom Firebase filter before the standard authentication filter
                .addFilterBefore(new FirebaseFilter(userService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

/**
 * Custom filter to process Firebase ID tokens from the Authorization header.
 */
class FirebaseFilter extends OncePerRequestFilter {

    private final UserService userService;

    public FirebaseFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // If the header is missing or doesn't start with "Bearer ", continue the filter chain.
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7);
        try {
            // Verify the Firebase ID token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();

            // Fetch the user from the local database using the email from the token
            com.example.patientdonation.entity.User user = userService.findByEmail(email).orElse(null);

            // If the user exists in the database, set the authentication context
            if (user != null) {
                // Create an authentication token with the User object as the principal
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, new ArrayList<>());

                // Set the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // If the token is invalid, clear the context to ensure the user is not authenticated
            SecurityContextHolder.clearContext();
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}