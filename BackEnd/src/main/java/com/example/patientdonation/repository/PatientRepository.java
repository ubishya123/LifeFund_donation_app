package com.example.patientdonation.repository;

import com.example.patientdonation.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.receivedAmount <p.requiredAmount ORDER BY p.createdAt DESC")
    List<Patient> findFeaturedPatients();
}
