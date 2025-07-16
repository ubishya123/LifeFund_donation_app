package com.example.patientdonation.service.impl;

import com.example.patientdonation.entity.AuthUser;
import com.example.patientdonation.repository.AuthUserRepository;
import com.example.patientdonation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private EmailService emailService; // 🔹 Injecting EmailService here

    // 🔹 Method to send OTP
    public String sendOtp(String email) {
        String otp = generateOtp();

        // Save or update AuthUser with OTP
        AuthUser user = authUserRepository.findByEmail(email)
                .orElse(new AuthUser());

        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpGeneratedTime(currentTime());
        user.setVerified(false);

        authUserRepository.save(user);

        // 🔹 Send OTP via email
        emailService.sendOtpEmail(email, otp);

        return "OTP sent to your email.";
    }

    // 🔹 Method to verify OTP

    @Autowired
    private UserService userService;

    public String verifyOtp(String email, String otp,String role,String name,String phone) {
        Optional<AuthUser> optionalUser = authUserRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "User not found!";
        }

        AuthUser user = optionalUser.get();

        if (user.getOtp().equals(otp)) {
            user.setVerified(true);
            authUserRepository.save(user);
            //auto create user
            userService.createUserIfNotExist(email,role,name,phone);
            return "OTP verified successfully!";
        } else {
            return "Invalid OTP!";
        }
    }

    // 🔹 Helper method to generate random 6-digit OTP
    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    // 🔹 Helper method to get current timestamp as string
    private String currentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
