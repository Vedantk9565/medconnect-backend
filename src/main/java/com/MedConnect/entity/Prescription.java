package com.MedConnect.entity;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.entity.Patient;  

@CrossOrigin(origins = "https://medconnect-frontend-1.onrender.com")
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;  // Establishes relationship with Patient entity

    @ManyToOne
    @JoinColumn(name = "medicine_id", referencedColumnName = "id")
    private Medicine medicine;  // Reference to the Medicine entity

    private String dosage;

    @Column(name = "time_to_take")
    private String timeToTake; // e.g., "Morning", "Afternoon", "Night"

    // Constructors
    public Prescription() {}

    public Prescription(Patient patient, Medicine medicine, String dosage, String timeToTake) {
        this.patient = patient;  // Directly using Patient entity
        this.medicine = medicine;  // Directly using Medicine entity
        this.dosage = dosage;
        this.timeToTake = timeToTake;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
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
