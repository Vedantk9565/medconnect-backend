package com.MedConnect.doclogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.entity.Patient;
@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long>
{

}
