package com.MedConnect.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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


import com.MedConnect.entity.Patient;
import com.MedConnect.entity.Prescription;
import com.MedConnect.repository.PrescriptionRepository;
import com.MedConnect.service.PatientService;
import com.MedConnect.service.TwilioService;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

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

            // 🔽 Define the external directory where PDF will be stored
            String basePath = "/tmp/uploads/prescriptions/";  // Use /tmp directory on Render
            String pdfFileName = id + ".pdf";
            String pdfPath = basePath + pdfFileName;

            // 🔽 Generate the PDF and save it
            createPdfForPatient(patient, pdfPath);

            // 🔽 Public URL pointing to your new custom endpoint
            String pdfUrl = "https://medconnect-backend-283p.onrender.com/api/v1/prescriptions/files/" + pdfFileName;

            // 🔽 WhatsApp message caption
            String caption = "Hello " + patient.getName() + ", please find your prescription attached.";

            // 🔽 Send the PDF via WhatsApp
            twilioService.sendWhatsAppMessageWithMedia(patient.getPhoneNumber(), pdfUrl, caption);

            return ResponseEntity.ok("Prescription PDF sent successfully via WhatsApp!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending prescription!");
        }
    }

    
 // ⬇️ Put the PDF creation method right here
    private void createPdfForPatient(Patient patient, String filePath) throws IOException {
        File directory = new File(filePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add logo (optional)
        Image logo = new Image(ImageDataFactory.create("https://medconnect-backend-283p.onrender.com/assets/medconnect2-logo.png"));
        logo.setWidth(100).setHeight(100).setFixedPosition(500, 750);
        document.add(logo);

        // Add title
        document.add(new Paragraph("Prescription")
                       .setBold()
                       .setFontSize(18)
                       .setTextAlignment(TextAlignment.CENTER)
                       .setFontColor(ColorConstants.BLUE));


        // Add patient details in a table with a background color
        float[] columnWidths = {2, 4};
        Table table = new Table(columnWidths);

        // Table Header
        table.addCell(new Cell().add(new Paragraph("Patient Name:")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addCell(new Cell().add(new Paragraph(patient.getName())));

        table.addCell(new Cell().add(new Paragraph("Age:")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getAge()))));

        table.addCell(new Cell().add(new Paragraph("Blood Group:")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addCell(new Cell().add(new Paragraph(patient.getBlood())));

        table.addCell(new Cell().add(new Paragraph("Prescription:")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addCell(new Cell().add(new Paragraph(patient.getPrescription())));

        table.addCell(new Cell().add(new Paragraph("Fees:")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getFees()))));

        document.add(table);

        // Footer message
        document.add(new Paragraph("Thank you for using MedConnect!").setTextAlignment(TextAlignment.CENTER));

        // Close the document
        document.close();
    }



    	@GetMapping("/files/{filename:.+}")
    	public ResponseEntity<?> servePrescriptionPdf(@PathVariable String filename) {
        try {
            String filePath =  "/tmp/uploads/prescriptions/" + filename;
            File file = new File(filePath);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + file.getName() + "\"")
                    .header("Content-Type", "application/pdf")
                    .body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file.");
        }
    }



   

}
