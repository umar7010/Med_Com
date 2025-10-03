package com.medicine.site.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_connection")
public class TestEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "test_message")
    private String testMessage;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    // Default constructor
    public TestEntity() {
        this.createdAt = java.time.LocalDateTime.now();
    }
    
    // Constructor with message
    public TestEntity(String testMessage) {
        this();
        this.testMessage = testMessage;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTestMessage() {
        return testMessage;
    }
    
    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }
    
    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
