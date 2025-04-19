package com.MedConnect.service;

import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.doclogin.repository.MedicineRepository;
import java.util.List;



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
    public Medicine getMedicineByName(String name) {
    	return medicineRepository.findByDrugNameIgnoreCase(name).orElse(null);

    
    
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

}
