package com.MedConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MedConnect.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>
{
	

}
