package com.example.patientdonation.service;

import com.example.patientdonation.entity.PatientUpdate;
import java.util.List;

public interface PatientUpdateService {

    /**
     * Adds a new update for a specific patient.
     * @param patientId The ID of the patient posting the update.
     * @param update The update content.
     * @return The saved PatientUpdate object.
     */
    PatientUpdate addUpdate(Long patientId, PatientUpdate update);

    /**
     * Retrieves all updates for a specific patient, ordered by most recent first.
     * @param patientId The ID of the patient.
     * @return A list of PatientUpdate objects.
     */
    List<PatientUpdate> getUpdatesForPatient(Long patientId);
}