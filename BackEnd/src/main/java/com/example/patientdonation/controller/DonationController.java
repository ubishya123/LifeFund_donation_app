package com.example.patientdonation.controller;
import com.example.patientdonation.dto.DonationDetailDTO;
import com.example.patientdonation.entity.Donation;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.DonationRepository;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.service.impl.DonationService;
import com.example.patientdonation.service.impl.RazorpayService;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/donation")
public class DonationController {
    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationService donationService;

    @PostMapping("/create-order/{patientId}")
    public String createOrder(
            @PathVariable Long patientId,
            @RequestParam int amount,
            @RequestParam String donorEmail,   // NEW
            @RequestParam(required = false) String donorName // Optional
    ) throws Exception {
        Order order = razorpayService.createOrder(amount);

        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setCurrency("INR");
        donation.setRazorpayOrderId(order.get("id"));
        donation.setStatus("PENDING");

        donation.setDonorEmail(donorEmail);
        donation.setDonorName(donorName != null ? donorName : "Anonymous");

        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        patientOpt.ifPresent(donation::setPatient);

        donationRepository.save(donation);

        return order.toString();
    }

    @PostMapping("/verify")
    public String verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature,
            @RequestParam Long donationId
    ) {
        boolean verified = razorpayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature, "A0p6P2LrKqaUyFAjwRDNVsAO");

        if (verified) {
            Donation donation = donationRepository.findById(donationId).orElse(null);
            if (donation != null) {
                donation.setStatus("SUCCESS");
                donation.setRazorpayPaymentId(razorpayPaymentId);
                donation.setRazorpaySignature(razorpaySignature);

                // Update donated amount in patient
                Patient patient = donation.getPatient();
                patient.setReceivedAmount(patient.getReceivedAmount() + donation.getAmount());

                patientRepository.save(patient);
                donationRepository.save(donation);
            }
            return "Payment verified successfully";
        } else {
            return "Payment verification failed";
        }
    }

    @GetMapping("/total-donation/{patientId}")
    public ResponseEntity<Double> getTotalDonation(@PathVariable Long patientId) {
        Double total = donationService.getTotalDonationForPatient(patientId);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    @GetMapping("/total")
    public double getTotalDonations() {
        return donationRepository.findAll()
                .stream().mapToDouble(Donation::getAmount).sum();
    }

    @GetMapping("/by-donor")
    public ResponseEntity<List<Donation>> getDonationsByDonor(@RequestParam String email) {
        return ResponseEntity.ok(donationService.getDonationsByEmail(email));
    }

    @GetMapping("/all-details")
    public ResponseEntity<List<DonationDetailDTO>> getAllDonationDetails() {
        return ResponseEntity.ok(donationService.getAllDonationDetails());
    }





}
