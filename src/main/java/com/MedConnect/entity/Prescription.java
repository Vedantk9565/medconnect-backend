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

    // Constructors
    public Prescription() {}

    public Prescription(Long patientId, Long medicineId, String dosage) {
        this.patientId = patientId;
        this.medicineId = medicineId;
        this.dosage = dosage;
    }

    // Getters and setters
    // ...
    
    
}
