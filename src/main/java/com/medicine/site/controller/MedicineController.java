package com.medicine.site.controller;

import com.medicine.site.entity.Medicine;
import com.medicine.site.entity.User;
import com.medicine.site.service.CartService;
import com.medicine.site.service.MedicineService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/medicines")
public class MedicineController {
    
    @Autowired
    private MedicineService medicineService;
    
    @Autowired
    private CartService cartService;
    
    /**
     * Show medicine catalog with search and filtering
     */
    @GetMapping
    public String showMedicineCatalog(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "prescription", required = false) String prescription,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size,
            HttpSession session,
            Model model) {
        
        // Get user from session (can be null)
        User user = (User) session.getAttribute("user");
        
        // Create pageable object
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Get medicines based on filters
        Page<Medicine> medicines;
        
        if (search != null && !search.trim().isEmpty()) {
            medicines = medicineService.searchMedicines(search.trim(), pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            medicines = medicineService.getMedicinesByCategory(category, pageable);
        } else if (minPrice != null || maxPrice != null) {
            BigDecimal min = minPrice != null ? minPrice : BigDecimal.ZERO;
            BigDecimal max = maxPrice != null ? maxPrice : new BigDecimal("999999");
            medicines = medicineService.getMedicinesByPriceRange(min, max, pageable);
        } else if ("prescription".equals(prescription)) {
            medicines = medicineService.getPrescriptionMedicines().isEmpty() ? 
                       Page.empty(pageable) : 
                       medicineService.getMedicinesInStock(pageable);
        } else if ("non-prescription".equals(prescription)) {
            medicines = medicineService.getNonPrescriptionMedicines().isEmpty() ? 
                       Page.empty(pageable) : 
                       medicineService.getMedicinesInStock(pageable);
        } else {
            medicines = medicineService.getAllMedicines(pageable);
        }
        
        // Get filter options
        List<String> categories = medicineService.getAllCategories();
        List<String> manufacturers = medicineService.getAllManufacturers();
        
        // Get cart info (only if user is logged in)
        Long cartItemCount = (user != null) ? cartService.getCartItemCount(user.getId()) : 0L;
        
        // Add to model
        model.addAttribute("medicines", medicines);
        model.addAttribute("categories", categories);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("user", user);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentCategory", category);
        model.addAttribute("currentMinPrice", minPrice);
        model.addAttribute("currentMaxPrice", maxPrice);
        model.addAttribute("currentPrescription", prescription);
        model.addAttribute("currentSortBy", sortBy);
        model.addAttribute("currentSortDir", sortDir);
        
        return "medicines/simple-catalog";
    }
    
    /**
     * Show medicine details
     */
    @GetMapping("/{id}")
    public String showMedicineDetails(@PathVariable Long id, HttpSession session, Model model) {
        // Get user from session (can be null)
        User user = (User) session.getAttribute("user");
        
        // Get medicine details
        Medicine medicine = medicineService.getMedicineById(id).orElse(null);
        if (medicine == null || !medicine.getIsActive()) {
            return "redirect:/medicines";
        }
        
        // Get cart info (only if user is logged in)
        Long cartItemCount = (user != null) ? cartService.getCartItemCount(user.getId()) : 0L;
        
        model.addAttribute("medicine", medicine);
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("user", user);
        
        return "medicines/details";
    }
    
    /**
     * Add medicine to cart
     */
    @PostMapping("/{id}/add-to-cart")
    public String addToCart(@PathVariable Long id, 
                           @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        try {
            cartService.addToCart(user.getId(), id, quantity);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Medicine added to cart successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/medicines/" + id;
    }
    
    /**
     * Quick add to cart from catalog
     */
    @PostMapping("/quick-add/{id}")
    public String quickAddToCart(@PathVariable Long id,
                               @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                               @RequestParam(value = "returnUrl", defaultValue = "/medicines") String returnUrl,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        try {
            cartService.addToCart(user.getId(), id, quantity);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Medicine added to cart successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:" + returnUrl;
    }
}
