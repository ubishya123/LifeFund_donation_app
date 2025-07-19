package com.example.patientdonation.service.impl;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.util.Utils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.razorpay.*;
@Service
public class RazorpayService {

    private final RazorpayClient client;

    public RazorpayService() throws RazorpayException{
        this.client=new RazorpayClient("rzp_test_ZQLpkKI7CM8kNh","A0p6P2LrKqaUyFAjwRDNVsAO");

    }

    public Order createOrder(int amount) throws RazorpayException{
        JSONObject options=new JSONObject();

        options.put("amount",amount*100);
        options.put("currency","INR");
        options.put("payment_capture",1);

        return client.orders.create(options);
    }

    public boolean verifySignature(String orderId,String paymentId,String signature,String secret)
    {
        try{
            String payload=orderId+"|"+paymentId;
            String actualSignature=Utils.calculateRFC2104HMAC(payload,secret);
            return actualSignature.equals(signature);

        }
        catch(Exception e)
        {
            return false;
        }
    }

    // In your RazorpayService.java

    public String createLinkedAccount(Patient patient) throws RazorpayException {
        JSONObject accountRequest = new JSONObject();
        accountRequest.put("type", "bank_account");
        accountRequest.put("email", patient.getEmail()); // Make sure patient has an email property

        // You can add more details about the linked account as needed
        accountRequest.put("name", patient.getName());
        accountRequest.put("phone", patient.getPhoneNumber());
        accountRequest.put("legal_business_name", patient.getName());

        JSONObject bankAccount = new JSONObject();
        bankAccount.put("ifsc_code", patient.getIfscCode());
        bankAccount.put("account_number", patient.getAccountNumber());
        bankAccount.put("name", patient.getName());
        accountRequest.put("bank_account", bankAccount);

        // This is the correct way to create an account with your library version
        Account account = client.account.create(accountRequest);
        return account.get("id");
    }

}
