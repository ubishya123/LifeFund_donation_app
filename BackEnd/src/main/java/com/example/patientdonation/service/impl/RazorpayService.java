package com.example.patientdonation.service.impl;

import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.util.Utils;
import com.razorpay.Account;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient client;
    private final String keySecret; // Store the secret for signature verification

    /**
     * Secure constructor that injects keys from application.properties.
     * Spring Boot will automatically populate keyId and keySecret from your environment variables.
     *
     * @param keyId     The Razorpay Key ID from environment variables.
     * @param keySecret The Razorpay Key Secret from environment variables.
     * @throws RazorpayException if the client cannot be initialized.
     */
    public RazorpayService(@Value("${razorpay.key_id}") String keyId,
                           @Value("${razorpay.key_secret}") String keySecret) throws RazorpayException {
        this.client = new RazorpayClient(keyId, keySecret);
        this.keySecret = keySecret;
    }

    /**
     * Creates a Razorpay order. This method now accepts a JSONObject to support
     * advanced features like direct transfers (Razorpay Route).
     *
     * @param orderRequest The JSON object containing order details (amount, currency, transfers, etc.).
     * @return The created Razorpay Order object.
     * @throws RazorpayException if the order creation fails.
     */
    public Order createOrder(JSONObject orderRequest) throws RazorpayException {
        return client.orders.create(orderRequest);
    }

    /**
     * Verifies the payment signature returned by Razorpay.
     * This is a critical security step to confirm the payment is authentic.
     *
     * @param orderId   The ID of the order from Razorpay.
     * @param paymentId The ID of the payment from Razorpay.
     * @param signature The signature provided by Razorpay in the callback.
     * @return true if the signature is valid, false otherwise.
     */
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            // Securely verifies the signature using the secret key from the environment.
            String actualSignature = Utils.calculateRFC2104HMAC(payload, this.keySecret);
            return actualSignature.equals(signature);
        } catch (Exception e) {
            // Log the error in a real application
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a "Linked Account" for a patient to enable direct fund transfers.
     *
     * @param patient The patient entity containing bank account details.
     * @return The unique ID of the created Razorpay Linked Account.
     * @throws RazorpayException if the account creation fails.
     */
    public String createLinkedAccount(Patient patient) throws RazorpayException {
        JSONObject accountRequest = new JSONObject();
        accountRequest.put("type", "bank_account");
        accountRequest.put("email", patient.getEmail());
        accountRequest.put("name", patient.getName());
        accountRequest.put("phone", patient.getPhoneNumber());
        accountRequest.put("legal_business_name", patient.getName());

        JSONObject bankAccount = new JSONObject();
        bankAccount.put("ifsc_code", patient.getIfscCode());
        bankAccount.put("account_number", patient.getAccountNumber());
        bankAccount.put("name", patient.getName());
        accountRequest.put("bank_account", bankAccount);

        Account account = client.account.create(accountRequest);
        return account.get("id");
    }
}
