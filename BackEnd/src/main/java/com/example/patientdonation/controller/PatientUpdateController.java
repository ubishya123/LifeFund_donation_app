package com.example.patientdonation.controller;

import com.example.patientdonation.entity.PatientUpdate;
import com.example.patientdonation.service.PatientUpdateService; // Import the service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/updates")
public class PatientUpdateController {

    // --- USE THE SERVICE INSTEAD OF THE REPOSITORY ---
    @Autowired
    private PatientUpdateService patientUpdateService;

    // Endpoint for patients to post a new update
    @PostMapping
    public ResponseEntity<PatientUpdate> addUpdate(@PathVariable Long patientId, @RequestBody PatientUpdate update) {
        // In a real app, you would also verify that the logged-in user's ID
        // matches the patientId to ensure they can only post updates for themselves.
        PatientUpdate savedUpdate = patientUpdateService.addUpdate(patientId, update);
        return ResponseEntity.ok(savedUpdate);
    }

    // Endpoint for anyone to view all updates for a patient
    @GetMapping
    public ResponseEntity<List<PatientUpdate>> getUpdates(@PathVariable Long patientId) {
        List<PatientUpdate> updates = patientUpdateService.getUpdatesForPatient(patientId);
        return ResponseEntity.ok(updates);
    }
}