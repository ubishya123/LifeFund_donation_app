package com.example.patientdonation.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.example.patientdonation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService; // Autowire UserService

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow public GET requests for patients, comments, and uploads
                        .requestMatchers(HttpMethod.GET, "/api/patients/**", "/uploads/**").permitAll()
                        // Allow public registration
                        .requestMatchers("/api/auth/**").permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Pass the userService to the filter
                .addFilterBefore(new FirebaseFilter(userService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

// Custom Filter to process Firebase tokens
// This is the inner class FirebaseFilter inside your SecurityConfig.java

class FirebaseFilter extends OncePerRequestFilter {

    private final UserService userService;

    public FirebaseFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7);
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();

            // --- THIS IS THE CORRECTED LOGIC ---
            // 1. Fetch the user directly from the database
            com.example.patientdonation.entity.User user = userService.findByEmail(email).orElse(null);

            // 2. Check if the user exists in your database
            if (user != null) {
                // 3. Create an authentication token with the User object as the principal
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, new ArrayList<>());

                // 4. Set the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // --- END OF CORRECTION ---

        } catch (Exception e) {
            // If the token is invalid or any other error occurs, clear the context
            SecurityContextHolder.clearContext();
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}