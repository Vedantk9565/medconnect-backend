package com.MedConnect.entity;

import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.persistence.*;
@CrossOrigin(origins = "https://medconnect-frontend-1.onrender.com")
@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long medicineId;
    private String dosage;
    @Column(name = "time_to_take")
    private String timeToTake; // e.g., "Morning", "Afternoon", "Night"

    
    // Constructors
    public Prescription() {}

    public Prescription(Long patientId, Long medicineId, String dosage,String timeToTake) {
        this.patientId = patientId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.timeToTake = timeToTake;
    }

    // Getters and setters
    // ...
    
    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTimeToTake() {
        return timeToTake;
    }

    public void setTimeToTake(String timeToTake) {
        this.timeToTake = timeToTake;
    }
}
