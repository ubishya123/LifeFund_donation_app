package com.example.patientdonation.controller;

import com.example.patientdonation.entity.User;
import com.example.patientdonation.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody AuthRequest authRequest) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authRequest.getToken());
            String email = decodedToken.getEmail();

            // Use your existing service to create the user with a role
            User user = userService.createUserIfNotExist(email, authRequest.getRole(), decodedToken.getName(), decodedToken.getUid());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}

// Helper DTO class
class AuthRequest {
    private String token;
    private String role;
    // getters and setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}