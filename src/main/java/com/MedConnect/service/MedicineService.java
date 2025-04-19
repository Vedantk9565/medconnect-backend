package com.MedConnect.service;

import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.doclogin.repository.MedicineRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // Fetch medicine by its name (or any other identifier you prefer)
    public Medicine getMedicineByName(String medicineName) {
        return medicineRepository.findByDrugName(medicineName);
    }
}
