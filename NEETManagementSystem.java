package com.neet;


import com.neet.ui.HomePage;
import com.neet.ui.LoginFrame;
import com.neet.db.DatabaseConnection;
import javax.swing.*;
import java.sql.Connection;

/**
 * Main Application Entry Point
 * NEET Management System
 */
public class NEETManagementSystem {
    
    public static void main(String[] args) {
        // Set Look and Feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set Look and Feel: " + e.getMessage());
        }
        
        // Test database connection before launching UI
        System.out.println("============================================");
        System.out.println("NEET Management System - Starting...");
        System.out.println("============================================");
        
        boolean dbConnected = testDatabaseConnection();
        
        if (dbConnected) {
            System.out.println("✓ Database connection successful!");
            System.out.println("✓ Launching application...");
            
            SwingUtilities.invokeLater(() -> {
                HomePage homePage = new HomePage();  // Changed from LoginFrame
                homePage.setVisible(true);
            });
        } else {
            System.err.println("✗ Database connection failed!");
            System.err.println("Please check:");
            System.err.println("  1. Oracle Database is running");
            System.err.println("  2. Database credentials in DatabaseConnection.java");
            System.err.println("  3. JDBC driver (ojdbc8.jar) is in classpath");
            System.err.println("  4. schema.sql has been executed");
            
            // Show error dialog
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                    "Failed to connect to database!\n\n" +
                    "Please ensure:\n" +
                    "1. Oracle Database is running\n" +
                    "2. Correct credentials in DatabaseConnection.java\n" +
                    "3. schema.sql has been executed\n\n" +
                    "Check console for more details.",
                    "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            });
        }
    }
    
    /**
     * Test database connection
     */
    private static boolean testDatabaseConnection() {
        try {
            System.out.println("\nTesting database connection...");
            Connection conn = DatabaseConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database: Connected");
                System.out.println("Connection URL: " + conn.getMetaData().getURL());
                System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + conn.getMetaData().getDatabaseProductVersion());
                return true;
            }
        } catch (Exception e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**m
     * Display system information
     */
    public static void displaySystemInfo() { 
        System.out.println("\n============================================");
        System.out.println("System Information");
        System.out.println("============================================");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("============================================\n");
    }
}
