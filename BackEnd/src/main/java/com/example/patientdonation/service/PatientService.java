package com.example.patientdonation.service;

import com.example.patientdonation.dto.PatientDTO;
import com.example.patientdonation.entity.Patient;

import java.util.List;

public interface PatientService {

    Patient addPatient(PatientDTO dto);
    List<Patient> getAllPatients();
    Patient getPatientById(Long id);

    Patient updatePatient(Patient patient);
}
