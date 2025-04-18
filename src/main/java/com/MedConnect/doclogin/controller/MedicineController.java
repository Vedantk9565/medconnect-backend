package com.MedConnect.doclogin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MedConnect.doclogin.entity.Medicine;
import com.MedConnect.doclogin.repository.MedicineRepository;
@CrossOrigin(origins = "https://medconnect-frontend-1.onrender.com")
@RestController
@RequestMapping("/api/v3")
public class MedicineController {
	MedicineRepository medicineRepository;
	
	public MedicineController(MedicineRepository medicineRepository) {
		super();
		this.medicineRepository = medicineRepository;
	}
	@PostMapping("/medicines")
	public Medicine createMedicine(@RequestBody Medicine medicine) {
	return medicineRepository.save(medicine);
	}
	@GetMapping(value= "/medicines",produces = "application/json")
	public List<Medicine>getAllMedicine(){
		return medicineRepository.findAll();
	}
		
	@GetMapping("/medicines/{id}")
	public ResponseEntity<Medicine>  getMedicineById(@PathVariable long id) throws AttributeNotFoundException
	{
		Medicine medicine = medicineRepository.findById(id).orElseThrow(()-> new AttributeNotFoundException("Medicine Not Found With Id:"+id));
		
		return ResponseEntity.ok(medicine);
	}
	
	@PutMapping("/medicines/{id}")
	public ResponseEntity<Medicine> updateMedicine(@PathVariable long id,@RequestBody Medicine medicineDetails) throws AttributeNotFoundException
	{
		Medicine medicine=medicineRepository.findById(id).orElseThrow(() -> new AttributeNotFoundException("Medicine Not Found With Id:"+id));
		medicine.setDrugName(medicineDetails.getDrugName());
		medicine.setStock(medicineDetails.getStock());
		medicineRepository.save(medicine);
		return ResponseEntity.ok(medicine);
	}
	@DeleteMapping("/medicines/{id}")
	public ResponseEntity<Map<String,  Boolean>>delete(@PathVariable long id) throws AttributeNotFoundException
	{
		Medicine medicine = medicineRepository.findById(id).orElseThrow(()-> new AttributeNotFoundException("Medicine Not Found With Id:"+id));
		
		medicineRepository.delete(medicine);
		Map<String, Boolean> response = new HashMap<String,Boolean>();
		response.put("Delete",Boolean.TRUE);
		return ResponseEntity.ok(response);
				
	}
	
	
	
	}
