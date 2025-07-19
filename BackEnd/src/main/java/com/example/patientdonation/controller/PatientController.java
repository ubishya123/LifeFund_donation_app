package com.example.patientdonation.controller;
import com.example.patientdonation.dto.PatientDonationStatusDTO;
import com.example.patientdonation.entity.Donation;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.dto.PatientDTO;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.service.PatientService;
import com.example.patientdonation.service.impl.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {


    @Autowired
    private PatientService patientService;
    @Autowired
    private DonationService donationService;
    @PostMapping("/add")
    public Patient addPatient(@RequestBody PatientDTO dto)
    {
        return patientService.addPatient(dto);
    }

    @GetMapping("/all")
    public List<Patient> getAllPatients()
    {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id)
    {
        return patientService.getPatientById(id);
    }

    @PostMapping("/{id}/upload-report")
    public ResponseEntity<String> uploadReport(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        Patient patient = patientService.getPatientById(id);


        if(patient==null)throw new RuntimeException("Patient Not Found");

        String uploadDir = "uploads/reports/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        patient.setReportFilePath(filePath.toString());
        patient.setReportFileUrl("uploads/reports/"+fileName);
        patientService.updatePatient(patient);
        return ResponseEntity.ok("Report uploaded successfully.");
    }

    @GetMapping("/donation-status")
    public List<PatientDonationStatusDTO> getDonationStatusForAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return patients.stream().map(patient -> {
            double remaining = patient.getRequiredAmount() - patient.getReceivedAmount();
            return new PatientDonationStatusDTO(
                    patient.getId(),
                    patient.getName(),
                    patient.getRequiredAmount(),
                    patient.getReceivedAmount(),
                    remaining < 0 ? 0 : remaining
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/{patientId}/donations")
    public ResponseEntity<List<Donation>> getDonationForPatient(@PathVariable Long patientId){
        List<Donation> donations=donationService.getDonationForPatient(patientId);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Patient>> getFeaturedPatients() {
        List<Patient> featured=patientService.getFeaturedPatients();
        return ResponseEntity.ok(featured);

    }


}
