package com.example.patientdonation.controller;

import com.example.patientdonation.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email)
    {
        return authService.sendOtp(email);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,@RequestParam String otp,@RequestParam String role,@RequestParam String name,@RequestParam String phone)
    {
        return authService.verifyOtp(email,otp,role,name,phone);

    }
}