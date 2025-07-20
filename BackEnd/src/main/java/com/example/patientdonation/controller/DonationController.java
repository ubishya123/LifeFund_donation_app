package com.example.patientdonation.controller;

import com.example.patientdonation.dto.DonationDetailDTO;
import com.example.patientdonation.entity.Donation;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.entity.User;
import com.example.patientdonation.repository.DonationRepository;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.service.impl.DonationService;
import com.example.patientdonation.service.impl.RazorpayService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donation")
@CrossOrigin(origins = "*") // Added CrossOrigin
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
    public ResponseEntity<Map<String, Object>> createOrder( // Changed return type
                                                            @PathVariable Long patientId,
                                                            @RequestParam int amount,
                                                            @RequestParam String donorEmail,
                                                            @RequestParam(required = false) String donorName) throws RazorpayException {

        // 1. Fetch the patient to get their Linked Account ID.
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient Not Found with id: " + patientId));

        // 2. Build the order request with transfer details for Razorpay Route.
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paise
        orderRequest.put("currency", "INR");

        JSONArray transfers = new JSONArray();
        JSONObject transfer = new JSONObject();
        transfer.put("account", patient.getLinkedAccountId()); // The patient's linked account ID
        transfer.put("amount", amount * 100); // The full amount is transferred
        transfer.put("currency", "INR");
        transfers.put(transfer);

        orderRequest.put("transfers", transfers);

        // 3. Call the service with the complete orderRequest object.
        Order order = razorpayService.createOrder(orderRequest);

        // 4. Create and save the donation record in your database.
        Donation donation = new Donation();
        donation.setAmount(amount);
        donation.setCurrency("INR");
        donation.setRazorpayOrderId(order.get("id"));
        donation.setStatus("PENDING");
        donation.setDonorEmail(donorEmail);
        donation.setDonorName(donorName != null ? donorName : "Anonymous");
        donation.setPatient(patient);

        Donation savedDonation = donationRepository.save(donation);

        // 5. Create a response map to send back to the frontend
        Map<String, Object> response = new HashMap<>();
        response.put("order", new JSONObject(order.toString()).toMap());
        response.put("donationId", savedDonation.getId()); // Include the donation ID

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public String verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature,
            @RequestParam Long donationId) {

        // The secret key is handled securely inside the RazorpayService.
        boolean verified = razorpayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (verified) {
            Donation donation = donationRepository.findById(donationId).orElse(null);
            if (donation != null) {
                donation.setStatus("SUCCESS");
                donation.setRazorpayPaymentId(razorpayPaymentId);
                donation.setRazorpaySignature(razorpaySignature);

                // Update donated amount in the patient record
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

    @GetMapping("/recent")
    public ResponseEntity<List<Donation>> getRecentDonations() {
        List<Donation> recentDonations = donationService.getRecentDonations();
        return ResponseEntity.ok(recentDonations);
    }

    @GetMapping("/my-history")
    public ResponseEntity<List<Donation>> getMyDonationHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        com.example.patientdonation.entity.User currentUser = (com.example.patientdonation.entity.User) authentication.getPrincipal();
        List<Donation> myDonations = donationService.getDonationsByEmail(currentUser.getEmail());
        return ResponseEntity.ok(myDonations);
    }
}
