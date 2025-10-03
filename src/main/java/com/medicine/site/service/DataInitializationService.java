package com.medicine.site.service;

import com.medicine.site.entity.Medicine;
import com.medicine.site.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if medicines already exist
        if (medicineRepository.count() > 0) {
            return; // Data already exists
        }
        
        // Create sample medicines
        List<Medicine> medicines = Arrays.asList(
            // Pain Relief
            new Medicine("Paracetamol 500mg", "Acetaminophen", "Johnson & Johnson", "Pain Relief", 
                        "Effective pain reliever and fever reducer. Safe for adults and children.", 
                        new BigDecimal("5.99"), 100),
            
            new Medicine("Ibuprofen 400mg", "Ibuprofen", "Pfizer", "Pain Relief", 
                        "Anti-inflammatory pain reliever for headaches, muscle pain, and inflammation.", 
                        new BigDecimal("8.99"), 75),
            
            new Medicine("Aspirin 325mg", "Acetylsalicylic Acid", "Bayer", "Pain Relief", 
                        "Pain reliever and anti-inflammatory. Also used for heart health.", 
                        new BigDecimal("6.99"), 50),
            
            // Cold & Flu
            new Medicine("Cold & Flu Relief", "Pseudoephedrine + Acetaminophen", "Tylenol", "Cold & Flu", 
                        "Multi-symptom relief for cold and flu symptoms including congestion and fever.", 
                        new BigDecimal("12.99"), 30),
            
            new Medicine("Cough Syrup", "Dextromethorphan", "Robitussin", "Cold & Flu", 
                        "Effective cough suppressant for dry coughs. Alcohol-free formula.", 
                        new BigDecimal("9.99"), 25),
            
            new Medicine("Nasal Decongestant", "Oxymetazoline", "Afrin", "Cold & Flu", 
                        "Fast-acting nasal spray for congestion relief. 12-hour relief.", 
                        new BigDecimal("7.99"), 40),
            
            // Antibiotics (Prescription Required)
            new Medicine("Amoxicillin 500mg", "Amoxicillin", "GlaxoSmithKline", "Antibiotics", 
                        "Broad-spectrum antibiotic for bacterial infections. Prescription required.", 
                        new BigDecimal("25.99"), 20),
            
            new Medicine("Ciprofloxacin 500mg", "Ciprofloxacin", "Bayer", "Antibiotics", 
                        "Fluoroquinolone antibiotic for serious bacterial infections. Prescription required.", 
                        new BigDecimal("35.99"), 15),
            
            new Medicine("Azithromycin 250mg", "Azithromycin", "Pfizer", "Antibiotics", 
                        "Macrolide antibiotic for respiratory and skin infections. Prescription required.", 
                        new BigDecimal("29.99"), 18),
            
            // Digestive Health
            new Medicine("Omeprazole 20mg", "Omeprazole", "AstraZeneca", "Digestive Health", 
                        "Proton pump inhibitor for acid reflux and stomach ulcers. Prescription required.", 
                        new BigDecimal("18.99"), 35),
            
            new Medicine("Loperamide 2mg", "Loperamide", "Janssen", "Digestive Health", 
                        "Anti-diarrheal medication for acute and chronic diarrhea.", 
                        new BigDecimal("6.99"), 60),
            
            new Medicine("Simethicone 125mg", "Simethicone", "Johnson & Johnson", "Digestive Health", 
                        "Anti-gas medication for bloating and gas relief.", 
                        new BigDecimal("4.99"), 80),
            
            // Cardiovascular
            new Medicine("Atorvastatin 20mg", "Atorvastatin", "Pfizer", "Cardiovascular", 
                        "Statin medication for cholesterol management. Prescription required.", 
                        new BigDecimal("22.99"), 25),
            
            new Medicine("Lisinopril 10mg", "Lisinopril", "Merck", "Cardiovascular", 
                        "ACE inhibitor for blood pressure management. Prescription required.", 
                        new BigDecimal("15.99"), 30),
            
            new Medicine("Metoprolol 50mg", "Metoprolol", "AstraZeneca", "Cardiovascular", 
                        "Beta-blocker for heart conditions and blood pressure. Prescription required.", 
                        new BigDecimal("19.99"), 22),
            
            // Diabetes
            new Medicine("Metformin 500mg", "Metformin", "Bristol Myers Squibb", "Diabetes", 
                        "First-line treatment for type 2 diabetes. Prescription required.", 
                        new BigDecimal("12.99"), 40),
            
            new Medicine("Insulin Glargine", "Insulin Glargine", "Sanofi", "Diabetes", 
                        "Long-acting insulin for diabetes management. Prescription required.", 
                        new BigDecimal("45.99"), 10),
            
            new Medicine("Glucose Test Strips", "Glucose Oxidase", "Roche", "Diabetes", 
                        "Blood glucose test strips for diabetes monitoring.", 
                        new BigDecimal("24.99"), 50),
            
            // Vitamins & Supplements
            new Medicine("Vitamin D3 1000IU", "Cholecalciferol", "Nature Made", "Vitamins", 
                        "Essential vitamin D supplement for bone health and immune support.", 
                        new BigDecimal("9.99"), 100),
            
            new Medicine("Vitamin C 1000mg", "Ascorbic Acid", "Nature's Bounty", "Vitamins", 
                        "High-potency vitamin C for immune system support.", 
                        new BigDecimal("7.99"), 120),
            
            new Medicine("Multivitamin", "Multiple Vitamins", "Centrum", "Vitamins", 
                        "Complete daily multivitamin for overall health and wellness.", 
                        new BigDecimal("11.99"), 90),
            
            // Allergy
            new Medicine("Loratadine 10mg", "Loratadine", "Bayer", "Allergy", 
                        "Non-drowsy antihistamine for seasonal allergies.", 
                        new BigDecimal("8.99"), 65),
            
            new Medicine("Cetirizine 10mg", "Cetirizine", "Johnson & Johnson", "Allergy", 
                        "24-hour allergy relief for sneezing, runny nose, and itchy eyes.", 
                        new BigDecimal("6.99"), 70),
            
            new Medicine("Diphenhydramine 25mg", "Diphenhydramine", "Benadryl", "Allergy", 
                        "Antihistamine for allergy relief and sleep aid.", 
                        new BigDecimal("5.99"), 85),
            
            // Skin Care
            new Medicine("Hydrocortisone Cream 1%", "Hydrocortisone", "Johnson & Johnson", "Skin Care", 
                        "Topical corticosteroid for skin inflammation and itching.", 
                        new BigDecimal("7.99"), 45),
            
            new Medicine("Antifungal Cream", "Clotrimazole", "Bayer", "Skin Care", 
                        "Topical antifungal treatment for athlete's foot and ringworm.", 
                        new BigDecimal("9.99"), 35),
            
            new Medicine("Sunscreen SPF 50", "Zinc Oxide + Titanium Dioxide", "Neutrogena", "Skin Care", 
                        "Broad-spectrum sunscreen for UV protection.", 
                        new BigDecimal("12.99"), 25)
        );
        
        // Set additional properties for each medicine
        for (Medicine medicine : medicines) {
            // Set dosage form
            if (medicine.getName().contains("mg")) {
                medicine.setDosageForm("Tablet");
            } else if (medicine.getName().contains("Syrup") || medicine.getName().contains("Cream")) {
                medicine.setDosageForm("Topical");
            } else if (medicine.getName().contains("Spray")) {
                medicine.setDosageForm("Nasal Spray");
            } else if (medicine.getName().contains("Strips")) {
                medicine.setDosageForm("Test Strips");
            } else {
                medicine.setDosageForm("Capsule");
            }
            
            // Set strength
            if (medicine.getName().contains("500mg")) {
                medicine.setStrength("500mg");
            } else if (medicine.getName().contains("400mg")) {
                medicine.setStrength("400mg");
            } else if (medicine.getName().contains("325mg")) {
                medicine.setStrength("325mg");
            } else if (medicine.getName().contains("250mg")) {
                medicine.setStrength("250mg");
            } else if (medicine.getName().contains("20mg")) {
                medicine.setStrength("20mg");
            } else if (medicine.getName().contains("10mg")) {
                medicine.setStrength("10mg");
            } else if (medicine.getName().contains("1000IU")) {
                medicine.setStrength("1000IU");
            } else if (medicine.getName().contains("1000mg")) {
                medicine.setStrength("1000mg");
            } else if (medicine.getName().contains("SPF 50")) {
                medicine.setStrength("SPF 50");
            } else if (medicine.getName().contains("1%")) {
                medicine.setStrength("1%");
            }
            
            // Set prescription requirement
            if (medicine.getCategory().equals("Antibiotics") || 
                medicine.getCategory().equals("Cardiovascular") || 
                medicine.getCategory().equals("Diabetes") ||
                medicine.getName().contains("Omeprazole") ||
                medicine.getName().contains("Atorvastatin") ||
                medicine.getName().contains("Lisinopril") ||
                medicine.getName().contains("Metoprolol") ||
                medicine.getName().contains("Metformin") ||
                medicine.getName().contains("Insulin")) {
                medicine.setRequiresPrescription(true);
            }
            
            // Set image URL (placeholder)
            medicine.setImageUrl("/images/medicines/" + medicine.getName().toLowerCase().replaceAll("[^a-z0-9]", "-") + ".jpg");
        }
        
        // Save all medicines
        medicineRepository.saveAll(medicines);
        
        System.out.println("Sample medicine data initialized successfully!");
    }
}
