package com.medicine.site.controller;

import com.medicine.site.entity.Cart;
import com.medicine.site.entity.User;
import com.medicine.site.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * Show shopping cart
     */
    @GetMapping
    public String showCart(HttpSession session, Model model) {
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // Get cart items
        List<Cart> cartItems = cartService.getUserCart(user.getId());
        
        // Calculate totals
        BigDecimal totalValue = cartService.getTotalCartValue(user.getId());
        Long itemCount = cartService.getCartItemCount(user.getId());
        
        // Check for insufficient stock
        List<Cart> insufficientStockItems = cartService.checkCartStock(user.getId());
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalValue", totalValue);
        model.addAttribute("itemCount", itemCount);
        model.addAttribute("insufficientStockItems", insufficientStockItems);
        model.addAttribute("user", user);
        
        return "cart/cart";
    }
    
    /**
     * Update cart item quantity
     */
    @PostMapping("/update/{medicineId}")
    public String updateCartItem(@PathVariable Long medicineId,
                               @RequestParam Integer quantity,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        try {
            cartService.updateCartItemQuantity(user.getId(), medicineId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Cart updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    /**
     * Remove item from cart
     */
    @PostMapping("/remove/{medicineId}")
    public String removeFromCart(@PathVariable Long medicineId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        cartService.removeFromCart(user.getId(), medicineId);
        redirectAttributes.addFlashAttribute("successMessage", 
            "Item removed from cart successfully!");
        
        return "redirect:/cart";
    }
    
    /**
     * Clear entire cart
     */
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        cartService.clearCart(user.getId());
        redirectAttributes.addFlashAttribute("successMessage", 
            "Cart cleared successfully!");
        
        return "redirect:/cart";
    }
    
    /**
     * Get cart count (AJAX endpoint)
     */
    @GetMapping("/count")
    @ResponseBody
    public Long getCartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return 0L;
        }
        return cartService.getCartItemCount(user.getId());
    }
    
    /**
     * Get cart total (AJAX endpoint)
     */
    @GetMapping("/total")
    @ResponseBody
    public BigDecimal getCartTotal(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return BigDecimal.ZERO;
        }
        return cartService.getTotalCartValue(user.getId());
    }
}
