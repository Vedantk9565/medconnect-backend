package com.MedConnect.controller;

import com.MedConnect.service.TwilioService;
import com.MedConnect.entity.Patient;
import com.MedConnect.entity.Prescription;
import com.MedConnect.repository.PatientRepository;
import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.dto.PrescriptionRequest;
import model.MedicineWithTime;
import com.MedConnect.service.MedicineService;
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
    private final MedicineService medicineService;  // Add MedicineService as a field

    // Constructor injection for MedicineService
    public PatientController(PatientRepository patientRepository, TwilioService twilioService, MedicineService medicineService) {
        this.patientRepository = patientRepository;
        this.twilioService = twilioService;
        this.medicineService = medicineService;  // Initialize medicineService
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
            twilioService.sendWhatsAppMessageWithMedia(
                    request.getPhoneNumber(),
                    request.getMediaUrl(),
                    request.getMessage()
            );
            return ResponseEntity.ok("Prescription sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send prescription");
        }
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        return optionalPatient
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        Optional<Patient> existingPatientOpt = patientRepository.findById(id);
        if (existingPatientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Patient existingPatient = existingPatientOpt.get();

        // Update fields manually, or use a mapper
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setBlood(updatedPatient.getBlood());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setPrescription(updatedPatient.getPrescription());
        existingPatient.setFees(updatedPatient.getFees());

        Patient saved = patientRepository.save(existingPatient);
        return ResponseEntity.ok(saved);
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
        System.out.println("Available medicines:");
        medicineService.getAllMedicines().forEach(m -> System.out.println(m.getDrugName()));

        for (MedicineWithTime medicineWithTime : medicinesWithTime) {
            System.out.println("Received medicine: " + medicineWithTime.getMedicineName());
            for (String time : medicineWithTime.getTimeToTake()) {
                Prescription newPrescription = new Prescription();

                newPrescription.setPatient(patient); 

                Medicine medicine = medicineService.getMedicineByName(medicineWithTime.getMedicineName());
                if (medicine == null) {
                    System.out.println("Medicine not found: " + medicineWithTime.getMedicineName());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Medicine not found: " + medicineWithTime.getMedicineName());
                }

                newPrescription.setMedicine(medicine);
                newPrescription.setDosage("1 tablet");
                newPrescription.setTimeToTake(time);

                existingPrescriptions.add(newPrescription);
            }
        }


        patient.setPrescription(existingPrescriptions); // Update patient's prescription list
        patientRepository.save(patient); // Save the updated patient entity

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
    @GetMapping("/medicines")
    public List<Medicine> getAllMedicines() {
        return medicineService.getAllMedicines();
    }

}
