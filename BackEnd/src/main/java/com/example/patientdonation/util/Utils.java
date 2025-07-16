package com.example.patientdonation.util;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Utils {

    public static String calculateRFC2104HMAC(String data, String secret) throws Exception {
        String algorithm = "HmacSHA256";
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());

        // Convert to hex or base64 (Razorpay uses hex)
        StringBuilder hexString = new StringBuilder();
        for (byte b : rawHmac) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
