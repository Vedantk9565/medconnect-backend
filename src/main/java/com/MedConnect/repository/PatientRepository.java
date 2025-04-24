package com.MedConnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MedConnect.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>
{
	 List<Patient> findByNameContainingOrIdContaining(String name, String id);

}
