package com.example.patientdonation.service.impl;

import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.entity.PatientUpdate;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.repository.PatientUpdateRepository;
import com.example.patientdonation.service.PatientUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientUpdateServiceImpl implements PatientUpdateService {

    @Autowired
    private PatientUpdateRepository patientUpdateRepository;

    @Autowired
    private PatientRepository patientRepository; // To link the update to a patient

    @Override
    public PatientUpdate addUpdate(Long patientId, PatientUpdate update) {
        // 1. Find the patient who is posting the update.
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        // 2. Associate the update with the patient.
        update.setPatient(patient);

        // 3. Save the update to the database.
        return patientUpdateRepository.save(update);
    }

    @Override
    public List<PatientUpdate> getUpdatesForPatient(Long patientId) {
        return patientUpdateRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
}