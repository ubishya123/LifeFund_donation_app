package com.example.patientdonation.service.impl;

import com.example.patientdonation.dto.PatientDTO;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.service.PatientService;
import com.razorpay.RazorpayException;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RazorpayService razorpayService;



    @Override
    public Patient addPatient(PatientDTO dto)
    {
        Patient patient=new Patient();
        patient.setName(dto.getName());
        patient.setDisease(dto.getDiseases());
        patient.setHospital(dto.getHospital());
        patient.setAdharNumber(dto.getAdharNumber());
        patient.setAccountNumber(dto.getAccountNumber());
        patient.setIfscCode(dto.getIfscCode());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setReportFileUrl(dto.getReportFileUrl());
        patient.setRequiredAmount(dto.getRequiredAmount());
        patient.setEmail(dto.getEmail());

        patientRepository.save(patient);

        try {
            // You'll need to implement this method in RazorpayService
            String linkedAccountId = razorpayService.createLinkedAccount(patient);
            patient.setLinkedAccountId(dto.getLinkedaccountid()); // Add this field to your Patient entity
            patientRepository.save(patient);
        } catch (RazorpayException e) {
            // Handle exception
        }

        return patient;
    }

    @Override
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id)
    {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Patient updatePatient(Patient patient)
    {
        return patientRepository.save(patient);
    }

    public List<Patient> getFeaturedPatients(){
        return patientRepository.findFeaturedPatients().stream().limit(3).collect(Collectors.toList());
    }
}
