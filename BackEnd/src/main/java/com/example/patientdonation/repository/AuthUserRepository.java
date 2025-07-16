package com.example.patientdonation.repository;

import com.example.patientdonation.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {

    Optional<AuthUser> findByEmail(String email);
}
