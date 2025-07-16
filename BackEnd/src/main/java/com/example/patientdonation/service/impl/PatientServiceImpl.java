package com.example.patientdonation.service.impl;

import com.example.patientdonation.dto.PatientDTO;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.service.PatientService;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

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
        patient.setRequriedAmount(dto.getRequiredAmount());

        return patientRepository.save(patient);
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
}
