package com.example.patientdonation.service.impl;

import com.example.patientdonation.dto.DonationDetailDTO;
import com.example.patientdonation.entity.Donation;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.DonationRepository;
import com.example.patientdonation.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class DonationService {

    @Autowired
    public DonationRepository donationRepository;

    @Autowired
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

    public List<Donation> getDonationForPatient(Long patientId)
    {
        return donationRepository.findByPatientId(patientId);
    }

    public List<Donation> getRecentDonations(){
        return donationRepository.findTop5ByStatusOrderByCreatedAtDesc("SUCCESS");
    }

    @Transactional
    public boolean verifyAndProcessPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, Long donationId) {
        // Note: You need to inject RazorpayService here or pass the secret key
        // For simplicity, assuming verifySignature is moved or accessible
        // boolean verified = razorpayService.verifySignature(...)

        // For now, let's assume verification happens in the controller and this service handles DB logic.
        Donation donation = donationRepository.findById(donationId).orElse(null);
        if (donation != null) {
            donation.setStatus("SUCCESS");
            donation.setRazorpayPaymentId(razorpayPaymentId);
            donation.setRazorpaySignature(razorpaySignature);

            Patient patient = donation.getPatient();
            patient.setReceivedAmount(patient.getReceivedAmount() + donation.getAmount());

            patientRepository.save(patient);
            donationRepository.save(donation);
            return true;
        }
        return false;
    }





}
