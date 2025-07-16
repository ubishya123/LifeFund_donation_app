package com.example.patientdonation.repository;

import com.example.patientdonation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation,Long> {

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.patient.id = :patientId")
    Double getTotalDonatedAmountByPatientId(@Param("patientId") Long patientId);

    List<Donation> findByDonorEmail(String donorEmail);

}
