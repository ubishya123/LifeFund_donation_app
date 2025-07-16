package com.example.patientdonation.service;

import com.example.patientdonation.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    User createUserIfNotExist(String email, String role, String name, String phone);
}
