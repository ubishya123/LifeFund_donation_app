package com.example.patientdonation.service.impl;
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

}
