package com.example.patientdonation.repository;

import com.example.patientdonation.entity.PatientUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientUpdateRepository extends JpaRepository<PatientUpdate, Long> {
    List<PatientUpdate> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}