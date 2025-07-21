package com.example.patientdonation.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDonationStatusDTO {

    private Long id;
    private String name;
    private double requiredAmount;
    private double receivedAmount;
    private double remainingAmount;


}
