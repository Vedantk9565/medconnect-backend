package com.MedConnect.controller;

import com.MedConnect.service.TwilioService;
import com.MedConnect.entity.Patient;
import com.MedConnect.entity.Prescription;
import com.MedConnect.repository.PatientRepository;
import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.dto.PrescriptionRequest;
import model.MedicineWithTime;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/patients")
@CrossOrigin(origins = "https://medconnect-frontend-1.onrender.com")
public class PatientController {

    private final PatientRepository patientRepository;
    private final TwilioService twilioService;

    public PatientController(PatientRepository patientRepository, TwilioService twilioService) {
        this.patientRepository = patientRepository;
        this.twilioService = twilioService;
    }

    @PutMapping("/{id}/send-prescription")
    public ResponseEntity<String> sendPrescription(
            @PathVariable Long id,
            @RequestBody PrescriptionRequest request) {

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }

        try {
            // Call method that includes media (PDF)
            twilioService.sendWhatsAppMessageWithMedia(
                    request.getPhoneNumber(),
                    request.getMediaUrl(),
                    request.getMessage()
            );
            return ResponseEntity.ok("Prescription sent successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // 💥 This will print the actual error in your console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send prescription");
        }
    }



    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll(); // or from service
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        return optionalPatient
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/add-medicine")
    public ResponseEntity<?> assignMedicineToPatient(
            @PathVariable Long id,
            @RequestBody List<MedicineWithTime> medicinesWithTime) {

        Optional<Patient> patientOpt = patientRepository.findById(id);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }

        Patient patient = patientOpt.get();
        List<Prescription> existingPrescriptions = patient.getPrescription();

        if (existingPrescriptions == null) {
            existingPrescriptions = new java.util.ArrayList<>();
        }

        for (MedicineWithTime medicineWithTime : medicinesWithTime) {
            for (String time : medicineWithTime.getTimeToTake()) {
                Prescription newPrescription = new Prescription();
                newPrescription.setPatientId(patient.getId());

                // Dummy medicine for now
                Medicine medicine = new Medicine();
                medicine.setDrugName(medicineWithTime.getMedicineName());

                newPrescription.setMedicine(medicine);

                newPrescription.setDosage("1 tablet"); // or customize
                newPrescription.setTimeToTake(time);

                existingPrescriptions.add(newPrescription);
            }
        }

        patient.setPrescription(existingPrescriptions); // ✅ No more string!
        patientRepository.save(patient);

        return ResponseEntity.ok("Medicines assigned successfully");
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        try {
            if (!patientRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
            }
            patientRepository.deleteById(id);
            return ResponseEntity.ok("Patient deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting patient.");
        }
    }


}
