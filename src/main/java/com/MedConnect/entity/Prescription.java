package com.MedConnect.entity;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.MedConnect.doclogin.entity.Medicine;

@CrossOrigin(origins = "https://medconnect-frontend-1.onrender.com")
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    @ManyToOne
    @JoinColumn(name = "medicine_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Medicine medicine;  // Optional: if you want a reference to the Medicine entity

    private String dosage;

    @Column(name = "time_to_take")
    private String timeToTake; // e.g., "Morning", "Afternoon", "Night"

    // Constructors
    public Prescription() {}

    // Constructor updated to use the Medicine entity
    public Prescription(Long patientId, Medicine medicine, String dosage, String timeToTake) {
        this.patientId = patientId;
        this.medicine = medicine;  // Directly using Medicine entity
        this.dosage = dosage;
        this.timeToTake = timeToTake;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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
