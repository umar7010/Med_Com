package com.medicine.site.repository;

import com.medicine.site.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find all cart items for a user
    List<Cart> findByUserId(Long userId);
    
    // Find cart item by user and medicine
    Optional<Cart> findByUserIdAndMedicineId(Long userId, Long medicineId);
    
    // Check if cart item exists for user and medicine
    boolean existsByUserIdAndMedicineId(Long userId, Long medicineId);
    
    // Count total items in cart for a user
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    // Calculate total cart value for a user
    @Query("SELECT COALESCE(SUM(c.totalPrice), 0) FROM Cart c WHERE c.userId = :userId")
    java.math.BigDecimal getTotalCartValue(@Param("userId") Long userId);
    
    // Find cart items with medicine details
    @Query("SELECT c FROM Cart c JOIN FETCH c.medicine WHERE c.userId = :userId")
    List<Cart> findByUserIdWithMedicine(@Param("userId") Long userId);
    
    // Delete all cart items for a user
    void deleteByUserId(Long userId);
    
    // Delete specific cart item for a user
    void deleteByUserIdAndMedicineId(Long userId, Long medicineId);
    
    // Find cart items with low stock
    @Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.quantity > c.medicine.stockQuantity")
    List<Cart> findItemsWithInsufficientStock(@Param("userId") Long userId);
}
