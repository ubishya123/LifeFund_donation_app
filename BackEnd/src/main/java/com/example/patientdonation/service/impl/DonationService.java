package com.example.patientdonation.service.impl;

import com.example.patientdonation.dto.DonationDetailDTO;
import com.example.patientdonation.entity.Donation;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.DonationRepository;
import com.example.patientdonation.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class DonationService {

    public DonationRepository donationRepository;
    public PatientRepository patientRepository;
    public Double getTotalDonationForPatient(Long patientId) {
        return donationRepository.getTotalDonatedAmountByPatientId(patientId);
    }


    public Donation saveDonation(Donation donation) {
        // Save donation record
        Donation saved = donationRepository.save(donation);

        // Update patient's received amount
        Patient patient = saved.getPatient();
        double updatedAmount = patient.getReceivedAmount() + saved.getAmount();
        patient.setReceivedAmount(updatedAmount);
        patientRepository.save(patient);

        return saved;
    }

    public List<Donation> getDonationsByEmail(String donorEmail) {
        return donationRepository.findByDonorEmail(donorEmail);
    }

    public List<DonationDetailDTO> getAllDonationDetails() {
        List<Donation> donations = donationRepository.findAll();
        List<DonationDetailDTO> dtos = new ArrayList<>();

        for (Donation donation : donations) {
            DonationDetailDTO dto = new DonationDetailDTO();
            dto.setDonorEmail(donation.getDonorEmail());
            dto.setAmount(donation.getAmount());

            Patient patient = patientRepository.findById(donation.getPatient().getId())
                    .orElse(null);

            if (patient != null) {
                dto.setPatientName(patient.getName());
            }

            dtos.add(dto);
        }

        return dtos;
    }





}
