package com.neet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Class for NEET Management System
 * Handles Oracle Database connections
 */
public class DatabaseConnection {
    
    // Oracle Database connection parameters
    // Format: jdbc:oracle:thin:@hostname:port:SID
    // For Oracle XE (Express Edition): jdbc:oracle:thin:@localhost:1521:XE
    // For Oracle with Service Name: jdbc:oracle:thin:@hostname:port/servicename
    
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USER = "your_system";           // Change to your Oracle username
    private static final String DB_PASSWORD = "your_password";     // Change to your Oracle password
    
    private static Connection connection = null;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }
    
    /**
     * Get database connection (Singleton pattern)
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load Oracle JDBC Driver
                Class.forName("oracle.jdbc.driver.OracleDriver");
                
                // Create connection
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connection established successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found!");
            System.err.println("Add ojdbc8.jar to your classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection!");
            System.err.println("Check your Oracle DB URL, username, and password");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection!");
            e.printStackTrace();
        }
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        if (testConnection()) {
            System.out.println("✓ Connection successful!");
            try {
                System.out.println("Database: " + connection.getMetaData().getDatabaseProductName());
                System.out.println("Version: " + connection.getMetaData().getDatabaseProductVersion());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("✗ Connection failed!");
        }
        closeConnection();
    }
}
