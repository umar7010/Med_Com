package com.medicine.site.service;

import com.medicine.site.entity.Medicine;
import com.medicine.site.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicineService {
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    /**
     * Get all active medicines
     */
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findByIsActiveTrue();
    }
    
    /**
     * Get all active medicines with pagination
     */
    public Page<Medicine> getAllMedicines(Pageable pageable) {
        return medicineRepository.findByIsActiveTrue(pageable);
    }
    
    /**
     * Get medicine by ID
     */
    public Optional<Medicine> getMedicineById(Long id) {
        return medicineRepository.findById(id);
    }
    
    /**
     * Search medicines by search term
     */
    public List<Medicine> searchMedicines(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMedicines();
        }
        return medicineRepository.searchMedicines(searchTerm.trim());
    }
    
    /**
     * Search medicines with pagination
     */
    public Page<Medicine> searchMedicines(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllMedicines(pageable);
        }
        return medicineRepository.searchMedicines(searchTerm.trim(), pageable);
    }
    
    /**
     * Get medicines by category
     */
    public List<Medicine> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    /**
     * Get medicines by category with pagination
     */
    public Page<Medicine> getMedicinesByCategory(String category, Pageable pageable) {
        return medicineRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }
    
    /**
     * Get medicines by price range
     */
    public List<Medicine> getMedicinesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return medicineRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Get medicines by price range with pagination
     */
    public Page<Medicine> getMedicinesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return medicineRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }
    
    /**
     * Get medicines that require prescription
     */
    public List<Medicine> getPrescriptionMedicines() {
        return medicineRepository.findByRequiresPrescriptionTrueAndIsActiveTrue();
    }
    
    /**
     * Get medicines that don't require prescription
     */
    public List<Medicine> getNonPrescriptionMedicines() {
        return medicineRepository.findByRequiresPrescriptionFalseAndIsActiveTrue();
    }
    
    /**
     * Get medicines in stock
     */
    public List<Medicine> getMedicinesInStock() {
        return medicineRepository.findInStock();
    }
    
    /**
     * Get medicines in stock with pagination
     */
    public Page<Medicine> getMedicinesInStock(Pageable pageable) {
        return medicineRepository.findInStock(pageable);
    }
    
    /**
     * Get all unique categories
     */
    public List<String> getAllCategories() {
        return medicineRepository.findDistinctCategories();
    }
    
    /**
     * Get all unique manufacturers
     */
    public List<String> getAllManufacturers() {
        return medicineRepository.findDistinctManufacturers();
    }
    
    /**
     * Get medicines by multiple categories
     */
    public List<Medicine> getMedicinesByCategories(List<String> categories) {
        return medicineRepository.findByCategories(categories);
    }
    
    /**
     * Get medicines by multiple categories with pagination
     */
    public Page<Medicine> getMedicinesByCategories(List<String> categories, Pageable pageable) {
        return medicineRepository.findByCategories(categories, pageable);
    }
    
    /**
     * Save medicine
     */
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
    
    /**
     * Update medicine
     */
    public Medicine updateMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
    
    /**
     * Delete medicine (soft delete)
     */
    public void deleteMedicine(Long id) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        if (medicineOptional.isPresent()) {
            Medicine medicine = medicineOptional.get();
            medicine.setIsActive(false);
            medicineRepository.save(medicine);
        }
    }
    
    /**
     * Check if medicine is in stock
     */
    public boolean isMedicineInStock(Long medicineId, Integer requiredQuantity) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(medicineId);
        if (medicineOptional.isPresent()) {
            Medicine medicine = medicineOptional.get();
            return medicine.getStockQuantity() >= requiredQuantity;
        }
        return false;
    }
    
    /**
     * Update stock quantity
     */
    public void updateStockQuantity(Long medicineId, Integer newQuantity) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(medicineId);
        if (medicineOptional.isPresent()) {
            Medicine medicine = medicineOptional.get();
            medicine.setStockQuantity(newQuantity);
            medicineRepository.save(medicine);
        }
    }
    
    /**
     * Reduce stock quantity
     */
    public void reduceStockQuantity(Long medicineId, Integer quantity) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(medicineId);
        if (medicineOptional.isPresent()) {
            Medicine medicine = medicineOptional.get();
            int newQuantity = medicine.getStockQuantity() - quantity;
            medicine.setStockQuantity(Math.max(0, newQuantity));
            medicineRepository.save(medicine);
        }
    }
}
