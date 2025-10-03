package com.medicine.site.service;

import com.medicine.site.entity.Cart;
import com.medicine.site.entity.Medicine;
import com.medicine.site.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private MedicineService medicineService;
    
    /**
     * Add item to cart
     */
    public Cart addToCart(Long userId, Long medicineId, Integer quantity) {
        // Check if medicine exists and is in stock
        Optional<Medicine> medicineOptional = medicineService.getMedicineById(medicineId);
        if (medicineOptional.isEmpty()) {
            throw new RuntimeException("Medicine not found");
        }
        
        Medicine medicine = medicineOptional.get();
        if (!medicine.getIsActive()) {
            throw new RuntimeException("Medicine is not available");
        }
        
        if (medicine.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + medicine.getStockQuantity());
        }
        
        // Check if item already exists in cart
        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndMedicineId(userId, medicineId);
        
        if (existingCartItem.isPresent()) {
            // Update existing cart item
            Cart cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            if (medicine.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + medicine.getStockQuantity());
            }
            
            cartItem.setQuantity(newQuantity);
            return cartRepository.save(cartItem);
        } else {
            // Create new cart item
            Cart cartItem = new Cart(userId, medicine, quantity);
            return cartRepository.save(cartItem);
        }
    }
    
    /**
     * Get user's cart items
     */
    public List<Cart> getUserCart(Long userId) {
        return cartRepository.findByUserIdWithMedicine(userId);
    }
    
    /**
     * Update cart item quantity
     */
    public Cart updateCartItemQuantity(Long userId, Long medicineId, Integer newQuantity) {
        Optional<Cart> cartItemOptional = cartRepository.findByUserIdAndMedicineId(userId, medicineId);
        
        if (cartItemOptional.isEmpty()) {
            throw new RuntimeException("Cart item not found");
        }
        
        if (newQuantity <= 0) {
            // Remove item from cart
            cartRepository.deleteByUserIdAndMedicineId(userId, medicineId);
            return null;
        }
        
        // Check stock availability
        Optional<Medicine> medicineOptional = medicineService.getMedicineById(medicineId);
        if (medicineOptional.isPresent()) {
            Medicine medicine = medicineOptional.get();
            if (medicine.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + medicine.getStockQuantity());
            }
        }
        
        Cart cartItem = cartItemOptional.get();
        cartItem.setQuantity(newQuantity);
        return cartRepository.save(cartItem);
    }
    
    /**
     * Remove item from cart
     */
    public void removeFromCart(Long userId, Long medicineId) {
        cartRepository.deleteByUserIdAndMedicineId(userId, medicineId);
    }
    
    /**
     * Clear user's cart
     */
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
    
    /**
     * Get cart item count for user
     */
    public Long getCartItemCount(Long userId) {
        return cartRepository.countByUserId(userId);
    }
    
    /**
     * Get total cart value for user
     */
    public BigDecimal getTotalCartValue(Long userId) {
        return cartRepository.getTotalCartValue(userId);
    }
    
    /**
     * Check cart for insufficient stock
     */
    public List<Cart> checkCartStock(Long userId) {
        return cartRepository.findItemsWithInsufficientStock(userId);
    }
    
    /**
     * Get cart item by user and medicine
     */
    public Optional<Cart> getCartItem(Long userId, Long medicineId) {
        return cartRepository.findByUserIdAndMedicineId(userId, medicineId);
    }
    
    /**
     * Check if cart item exists
     */
    public boolean cartItemExists(Long userId, Long medicineId) {
        return cartRepository.existsByUserIdAndMedicineId(userId, medicineId);
    }
    
    /**
     * Get all cart items (for debugging)
     */
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }
}
