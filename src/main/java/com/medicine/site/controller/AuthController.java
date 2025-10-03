package com.medicine.site.controller;

import com.medicine.site.dto.UserLoginDto;
import com.medicine.site.dto.UserRegistrationDto;
import com.medicine.site.entity.User;
import com.medicine.site.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Show registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "auth/register";
    }
    
    /**
     * Process user registration
     */
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto registrationDto,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        
        // Check if passwords match
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.userRegistrationDto", "Passwords do not match");
            return "auth/register";
        }
        
        try {
            // Create new user
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(registrationDto.getPassword());
            user.setFullName(registrationDto.getFullName());
            user.setPhoneNumber(registrationDto.getPhoneNumber());
            user.setAddress(registrationDto.getAddress());
            user.setCity(registrationDto.getCity());
            user.setState(registrationDto.getState());
            user.setZipCode(registrationDto.getZipCode());
            
            // Register user
            User savedUser = userService.registerUser(user);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Welcome " + savedUser.getFullName() + "!");
            return "redirect:/auth/login";
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/register";
        }
    }
    
    /**
     * Show login form
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "auth/login";
    }
    
    /**
     * Process user login
     */
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("userLoginDto") UserLoginDto loginDto,
                             BindingResult bindingResult,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        
        try {
            // Authenticate user
            User user = userService.authenticateUser(loginDto.getUsernameOrEmail(), loginDto.getPassword());
            
            // Store user in session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Welcome back, " + user.getFullName() + "!");
            return "redirect:/dashboard";
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/login";
        }
    }
    
    /**
     * Logout user
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out successfully.");
        return "redirect:/auth/login";
    }
    
    /**
     * Show user profile
     */
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        return "auth/profile";
    }
    
    /**
     * Check if user is logged in (helper method)
     */
    private boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
}
