package com.example.patientdonation.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // The path to your service account key file
        String serviceAccountPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");

        if (serviceAccountPath == null) {
            System.out.println("WARNING: GOOGLE_APPLICATION_CREDENTIALS environment variable not set. Falling back to classpath resource.");
            // Fallback for local development: looks for the file in src/main/resources
            ClassPathResource serviceAccountResource = new ClassPathResource("service-account-key.json");
            InputStream serviceAccountStream = serviceAccountResource.getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        } else {
            // Production-ready: loads from the path specified in the environment variable
            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        }
    }
}