package com.medicine.site.service;

import com.medicine.site.entity.User;
import com.medicine.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Register a new user
     */
    public User registerUser(User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        
        return userRepository.save(user);
    }
    
    /**
     * Authenticate user login
     */
    public User authenticateUser(String usernameOrEmail, String password) {
        Optional<User> userOptional = findByUsernameOrEmail(usernameOrEmail);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found: " + usernameOrEmail);
        }
        
        User user = userOptional.get();
        
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return user;
    }
    
    /**
     * Find user by username or email
     */
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        // Try to find by username first
        Optional<User> user = userRepository.findByUsernameAndIsActiveTrue(usernameOrEmail);
        
        // If not found by username, try email
        if (user.isEmpty()) {
            user = userRepository.findByEmailAndIsActiveTrue(usernameOrEmail);
        }
        
        return user;
    }
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Update user profile
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Check if username exists
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Deactivate user account
     */
    public void deactivateUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(false);
            userRepository.save(user);
        }
    }
}
