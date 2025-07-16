package com.example.patientdonation.service.impl;

import com.example.patientdonation.dto.AdminSummaryDTO;
import com.example.patientdonation.entity.Donation;
import com.example.patientdonation.repository.DonationRepository;
import com.example.patientdonation.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DonationRepository donationRepository;

    public AdminSummaryDTO getSummary() {
        AdminSummaryDTO dto = new AdminSummaryDTO();
        dto.setTotalPatients(patientRepository.count());
        dto.setTotalDonations(donationRepository.count());
        dto.setTotalAmount(
                donationRepository.findAll().stream()
                        .mapToDouble(Donation::getAmount)
                        .sum()
        );
        return dto;
    }


}
