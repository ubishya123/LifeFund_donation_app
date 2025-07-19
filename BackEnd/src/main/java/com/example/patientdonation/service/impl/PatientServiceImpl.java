package com.example.patientdonation.service.impl;

import com.example.patientdonation.dto.PatientDTO;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.service.PatientService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RazorpayService razorpayService;

    /**
     * Adds a new patient, creates a Razorpay Linked Account, and saves everything
     * to the database in a single, efficient operation.
     *
     * @param dto The patient data transfer object from the frontend.
     * @return The saved Patient entity with the linkedAccountId.
     */
    @Override
    @Transactional // Ensures the entire method is a single transaction.
    public Patient addPatient(PatientDTO dto) {
        // 1. Create the patient object in memory from the DTO.
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setDisease(dto.getDiseases());
        patient.setHospital(dto.getHospital());
        patient.setAdharNumber(dto.getAdharNumber());
        patient.setAccountNumber(dto.getAccountNumber());
        patient.setIfscCode(dto.getIfscCode());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setReportFileUrl(dto.getReportFileUrl());
        patient.setRequiredAmount(dto.getRequiredAmount()); // Corrected spelling
        patient.setEmail(dto.getEmail());

        try {
            // 2. Create the Linked Account on Razorpay first.
            String linkedAccountId = razorpayService.createLinkedAccount(patient);

            // 3. Set the returned ID on the patient object.
            patient.setLinkedAccountId(linkedAccountId);

            // 4. Save the complete patient object to the database only ONCE.
            return patientRepository.save(patient);

        } catch (RazorpayException e) {
            // If creating the Razorpay account fails, the whole operation fails.
            // This prevents creating a patient who cannot receive funds.
            // In a real application, you would log this error.
            e.printStackTrace();
            throw new RuntimeException("Failed to create Razorpay Linked Account for patient: " + e.getMessage());
        }
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getFeaturedPatients() {
        return patientRepository.findFeaturedPatients().stream().limit(3).collect(Collectors.toList());
    }
}
