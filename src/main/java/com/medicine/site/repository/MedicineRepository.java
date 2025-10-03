package com.medicine.site.repository;

import com.medicine.site.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    // Find all active medicines
    List<Medicine> findByIsActiveTrue();
    
    // Find all active medicines with pagination
    Page<Medicine> findByIsActiveTrue(Pageable pageable);
    
    // Search medicines by name (case insensitive)
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND m.isActive = true")
    List<Medicine> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    // Search medicines by generic name (case insensitive)
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.genericName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND m.isActive = true")
    List<Medicine> findByGenericNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    // Search medicines by manufacturer (case insensitive)
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND m.isActive = true")
    List<Medicine> findByManufacturerContainingIgnoreCase(@Param("searchTerm") String manufacturer);
    
    // Find medicines by category
    List<Medicine> findByCategoryAndIsActiveTrue(String category);
    
    // Find medicines by category with pagination
    Page<Medicine> findByCategoryAndIsActiveTrue(String category, Pageable pageable);
    
    // Search medicines by multiple criteria
    @Query("SELECT m FROM Medicine m WHERE " +
           "(LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.genericName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND m.isActive = true")
    List<Medicine> searchMedicines(@Param("searchTerm") String searchTerm);
    
    // Search medicines with pagination
    @Query("SELECT m FROM Medicine m WHERE " +
           "(LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.genericName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND m.isActive = true")
    Page<Medicine> searchMedicines(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Find medicines by price range
    @Query("SELECT m FROM Medicine m WHERE m.price BETWEEN :minPrice AND :maxPrice AND m.isActive = true")
    List<Medicine> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                   @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    // Find medicines by price range with pagination
    @Query("SELECT m FROM Medicine m WHERE m.price BETWEEN :minPrice AND :maxPrice AND m.isActive = true")
    Page<Medicine> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                   @Param("maxPrice") java.math.BigDecimal maxPrice, 
                                   Pageable pageable);
    
    // Find medicines that require prescription
    List<Medicine> findByRequiresPrescriptionTrueAndIsActiveTrue();
    
    // Find medicines that don't require prescription
    List<Medicine> findByRequiresPrescriptionFalseAndIsActiveTrue();
    
    // Find medicines in stock
    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity > 0 AND m.isActive = true")
    List<Medicine> findInStock();
    
    // Find medicines in stock with pagination
    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity > 0 AND m.isActive = true")
    Page<Medicine> findInStock(Pageable pageable);
    
    // Get all unique categories
    @Query("SELECT DISTINCT m.category FROM Medicine m WHERE m.isActive = true ORDER BY m.category")
    List<String> findDistinctCategories();
    
    // Get all unique manufacturers
    @Query("SELECT DISTINCT m.manufacturer FROM Medicine m WHERE m.isActive = true ORDER BY m.manufacturer")
    List<String> findDistinctManufacturers();
    
    // Find medicines by multiple categories
    @Query("SELECT m FROM Medicine m WHERE m.category IN :categories AND m.isActive = true")
    List<Medicine> findByCategories(@Param("categories") List<String> categories);
    
    // Find medicines by multiple categories with pagination
    @Query("SELECT m FROM Medicine m WHERE m.category IN :categories AND m.isActive = true")
    Page<Medicine> findByCategories(@Param("categories") List<String> categories, Pageable pageable);
}
