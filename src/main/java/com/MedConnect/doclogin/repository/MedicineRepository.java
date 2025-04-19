package com.MedConnect.doclogin.repository;

import com.MedConnect.doclogin.entity.Medicine;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Custom query to find medicine by its drug name
	Optional<Medicine> findByDrugNameIgnoreCase(String drugName);


}
