package com.MedConnect.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.MedConnect.entity.Patient;
import com.MedConnect.entity.Prescription;
import com.MedConnect.repository.PrescriptionRepository;
import com.MedConnect.service.PatientService;
import com.MedConnect.service.TwilioService;

@CrossOrigin(origins = "https://medconnect-frontend-1.onrender.com")
@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private PatientService patientService; // Autowire PatientService

    // Add prescription for a patient
    @PostMapping
    public Prescription addPrescription(@RequestBody Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    // Get prescriptions for a specific patient
    @GetMapping("/{patientId}")
    public List<Prescription> getPrescriptionsForPatient(@PathVariable Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    // Send prescription to a patient's WhatsApp
    @PutMapping("/patients/{id}/send-prescription")
    public ResponseEntity<String> sendPrescription(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);

            if (patient == null || patient.getPhoneNumber() == null) {
                return ResponseEntity.badRequest().body("Patient or phone number not found.");
            }

            // Generate PDF
            String pdfFileName = id + ".pdf";
            String pdfPath = "src/main/resources/static/prescriptions/" + pdfFileName;
            createPdfForPatient(patient, pdfPath); // You’ll write this method

            // Publicly accessible URL (must match your deployment)
            String pdfUrl = "https://yourdomain.com/prescriptions/" + pdfFileName;
            String caption = "Hello " + patient.getName() + ", please find your prescription attached.";

            // Send via Twilio
            twilioService.sendWhatsAppMessageWithMedia(patient.getPhoneNumber(), pdfUrl, caption);

            return ResponseEntity.ok("Prescription PDF sent successfully via WhatsApp!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending prescription!");
        }
    }
    
 // ⬇️ Put the PDF creation method right here
    private void createPdfForPatient(Patient patient, String filePath) throws IOException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Prescription"));
        document.add(new Paragraph("Name: " + patient.getName()));
        document.add(new Paragraph("Age: " + patient.getAge()));
        document.add(new Paragraph("Blood Group: " + patient.getBlood()));
        document.add(new Paragraph("Prescription: " + patient.getPrescription()));
        document.add(new Paragraph("Fees: " + patient.getFees()));

        document.close();
    }


   

}
