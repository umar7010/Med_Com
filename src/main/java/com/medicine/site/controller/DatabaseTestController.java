package com.medicine.site.controller;

import com.medicine.site.entity.TestEntity;
import com.medicine.site.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private TestEntityRepository testEntityRepository;

    @GetMapping("/database")
    public String testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                return "✅ Database connection successful! " +
                       "Database: " + connection.getCatalog() + 
                       " | Driver: " + connection.getMetaData().getDriverName() +
                       " | URL: " + connection.getMetaData().getURL();
            } else {
                return "❌ Database connection failed - connection is null or closed";
            }
        } catch (SQLException e) {
            return "❌ Database connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/database/info")
    public String getDatabaseInfo() {
        try (Connection connection = dataSource.getConnection()) {
            return "Database Information:\n" +
                   "• Database Name: " + connection.getCatalog() + "\n" +
                   "• Driver Name: " + connection.getMetaData().getDriverName() + "\n" +
                   "• Driver Version: " + connection.getMetaData().getDriverVersion() + "\n" +
                   "• Database URL: " + connection.getMetaData().getURL() + "\n" +
                   "• Database Product: " + connection.getMetaData().getDatabaseProductName() + "\n" +
                   "• Database Version: " + connection.getMetaData().getDatabaseProductVersion();
        } catch (SQLException e) {
            return "❌ Failed to get database info: " + e.getMessage();
        }
    }

    @GetMapping("/jpa")
    public String testJpaConnection() {
        try {
            // Create a test entity
            TestEntity testEntity = new TestEntity("Database connection test successful!");
            TestEntity savedEntity = testEntityRepository.save(testEntity);
            
            // Retrieve all entities
            List<TestEntity> allEntities = testEntityRepository.findAll();
            
            return "✅ JPA/Hibernate test successful!\n" +
                   "• Saved entity ID: " + savedEntity.getId() + "\n" +
                   "• Total entities in database: " + allEntities.size() + "\n" +
                   "• Test message: " + savedEntity.getTestMessage() + "\n" +
                   "• Created at: " + savedEntity.getCreatedAt();
        } catch (Exception e) {
            return "❌ JPA/Hibernate test failed: " + e.getMessage();
        }
    }

    @GetMapping("/jpa/cleanup")
    public String cleanupTestData() {
        try {
            testEntityRepository.deleteAll();
            return "✅ Test data cleaned up successfully!";
        } catch (Exception e) {
            return "❌ Failed to cleanup test data: " + e.getMessage();
        }
    }
}
