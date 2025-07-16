package com.example.patientdonation.service.impl;

import com.example.patientdonation.entity.User;
import com.example.patientdonation.repository.UserRepository;
import com.example.patientdonation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUserIfNotExist(String email, String role, String name, String phone) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPhoneNumber(phone);
        user.setRole(role.toUpperCase()); // "PATIENT", "DONOR", or "ADMIN"
        user.setActive(true);

        return userRepository.save(user);
    }
}
