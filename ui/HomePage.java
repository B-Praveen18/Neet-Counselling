package com.neet.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Home Page - NEET Counselling Management System
 * MCC-style professional government portal design
 * Auto-detects user type (Student/College/Admin) based on credentials
 * Enhanced with navigation buttons for Quick Links, About, and Help & Support
 */
public class HomePage extends JFrame {
    
    // Official Government Colors - Enhanced
    private static final Color HEADER_BLUE = new Color(0, 102, 153);
    private static final Color ACCENT_BLUE = new Color(0, 123, 255);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_GRAY = new Color(220, 220, 220);
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    private static final Color SUCCESS_GREEN = new Color(40, 167, 69);
    
    public HomePage() {
        setTitle("NEET UG Counselling Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WHITE);
        
        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, HEADER_BLUE),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        
        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        logoTitlePanel.setBackground(WHITE);
        
        // Enhanced Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(85, 85));
        logoPanel.setBackground(HEADER_BLUE);
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HEADER_BLUE, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        logoPanel.setLayout(new GridBagLayout());
        
        JLabel logoText = new JLabel("<html><center><b>MCC</b><br><span style='font-size:11px;'>DGHS</span></center></html>");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoText.setForeground(WHITE);
        logoPanel.add(logoText);
        
        logoTitlePanel.add(logoPanel);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(WHITE);
        
        // Enhanced Main Title with better font
        JLabel mainTitle = new JLabel("Medical Counselling Committee (MCC), DGHS, Ministry");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(new Color(0, 82, 133));
        
        JLabel subTitle1 = new JLabel("of Health & Family Welfare, Government of India");
        subTitle1.setFont(new Font("Segoe UI", Font.BOLD, 24));
        subTitle1.setForeground(new Color(0, 82, 133));
        
        // Enhanced subtitle with attractive color
        JLabel subTitle2 = new JLabel("NEET UG Counselling for MBBS, BDS & B.Sc. Nursing 2025");
        subTitle2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subTitle2.setForeground(new Color(100, 100, 150));
        subTitle2.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        titlePanel.add(mainTitle);
        titlePanel.add(subTitle1);
        titlePanel.add(subTitle2);
        
        logoTitlePanel.add(titlePanel);
        headerPanel.add(logoTitlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_GRAY);
        
        // Navigation Panel with 4 buttons
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navButtonPanel.setBackground(HEADER_BLUE);
        navButtonPanel.setPreferredSize(new Dimension(1200, 48));
        
        JButton homeButton = createNavButton("Home");
        JButton aboutButton = createNavButton("About");
        JButton helpButton = createNavButton("Help & Support");
        
        // Add action listeners for dialog boxes
        aboutButton.addActionListener(e -> showAboutDialog());
        helpButton.addActionListener(e -> showHelpDialog());
        
        navButtonPanel.add(homeButton);
        navButtonPanel.add(aboutButton);
        navButtonPanel.add(helpButton);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(LIGHT_GRAY);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 20);
        
        JPanel loginFormPanel = createLoginFormPanel();
        centerPanel.add(loginFormPanel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        JPanel instructionsPanel = createInstructionsPanel();
        centerPanel.add(instructionsPanel, gbc);
        
        contentPanel.add(navButtonPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(WHITE);
        button.setBackground(HEADER_BLUE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HEADER_BLUE.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(HEADER_BLUE);
            }
        });
        
        return button;
    }
    
    private JPanel createLoginFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(45, 45, 45, 45)
        ));
        
        // Clean title without emoji
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(HEADER_BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Subtle welcome message
        JLabel welcomeLabel = new JLabel("Welcome! Please enter your credentials");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(TEXT_SECONDARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(welcomeLabel);
        formPanel.add(Box.createVerticalStrut(35));
        
        // Username label without icon
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        usernameLabel.setForeground(TEXT_PRIMARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        usernameField.setMaximumSize(new Dimension(420, 45));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Password label without icon
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        passwordLabel.setForeground(TEXT_PRIMARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordField.setMaximumSize(new Dimension(420, 45));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(35));
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonsPanel.setBackground(WHITE);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Clean buttons without emojis
        JButton loginButton = createStyledButton("Login", ACCENT_BLUE);
        loginButton.addActionListener(e -> handleAutoLogin(
            usernameField.getText().trim(),
            new String(passwordField.getPassword())
        ));
        
        JButton registerButton = createStyledButton("Register", SUCCESS_GREEN);
        registerButton.addActionListener(e -> handleRegister());
        
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);
        
        formPanel.add(buttonsPanel);
        
        // Enter key press on password field triggers login
        passwordField.addActionListener(e -> handleAutoLogin(
            usernameField.getText().trim(),
            new String(passwordField.getPassword())
        ));
        
        return formPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(WHITE);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(100, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add subtle rounded effect
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 0),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });
        
        return button;
    }
    
    private JPanel createInstructionsPanel() {
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBackground(new Color(0, 102, 153));
        instructionsPanel.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
        
        // Title without emoji
        JLabel titleLabel = new JLabel("Important Instructions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        instructionsPanel.add(titleLabel);
        instructionsPanel.add(Box.createVerticalStrut(25));
        
        String[] instructions = {
            "Confidentiality of Password is the sole responsibility of the candidate and all care must be taken accordingly.",
            "Candidates are advised to keep checking the website at frequent intervals.",
            "Never share your password and do not respond to anyone who asks you for your Login-ID/Password.",
            "It is strongly recommended that the candidate should not do any activity like reset password, forgot password with help of anyone.",
            "For security reasons, after finishing your work, please logout and close the browser."
        };
        
        for (String instruction : instructions) {
            JPanel instructionPanel = new JPanel(new BorderLayout(10, 0));
            instructionPanel.setBackground(new Color(0, 102, 153));
            instructionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            instructionPanel.setMaximumSize(new Dimension(550, 100));
            
            // Simple square bullet without emoji
            JLabel bulletLabel = new JLabel("■");
            bulletLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            bulletLabel.setForeground(WHITE);
            bulletLabel.setVerticalAlignment(SwingConstants.TOP);
            
            JLabel instructionLabel = new JLabel("<html><div style='width: 480px; line-height: 1.6;'>" + instruction + "</div></html>");
            instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            instructionLabel.setForeground(WHITE);
            instructionLabel.setVerticalAlignment(SwingConstants.TOP);
            
            instructionPanel.add(bulletLabel, BorderLayout.WEST);
            instructionPanel.add(instructionLabel, BorderLayout.CENTER);
            
            instructionsPanel.add(instructionPanel);
            instructionsPanel.add(Box.createVerticalStrut(15));
        }
        
        return instructionsPanel;
    }
    
    /**
     * Show Quick Links Dialog
     */
    private void showQuickLinksDialog() {
        JDialog dialog = new JDialog(this, "Quick Links", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel("Quick Links");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(HEADER_BLUE);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        
        String[] links = {
            "• User Guide / How to Register",
            "• Frequently Asked Questions (FAQs)",
            "• Contact Support",
            "• Download Prospectus"
        };
        
        for (String link : links) {
            JLabel linkLabel = new JLabel(link);
            linkLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            linkLabel.setForeground(TEXT_PRIMARY);
            panel.add(linkLabel);
            panel.add(Box.createVerticalStrut(10));
        }
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Show About Dialog
     */
    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "About NEET Counselling", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel("About NEET Counselling");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(HEADER_BLUE);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        
        String[] aboutText = {
            "NEET UG counselling is conducted for admission to MBBS,",
            "BDS & B.Sc. Nursing courses across India.",
            "",
            "Who can apply:",
            "• NEET qualified candidates with valid score and rank",
            "• Must complete registration and choice filling on time",
            "• Document verification is mandatory",
            "",
            "Process:",
            "1. Register with NEET details",
            "2. Fill college/course choices",
            "3. Wait for allotment round",
            "4. Pay fees if allotted",
            "5. Complete document verification"
        };
        
        for (String text : aboutText) {
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textLabel.setForeground(TEXT_PRIMARY);
            panel.add(textLabel);
            panel.add(Box.createVerticalStrut(5));
        }
        
        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }
    
    /**
     * Show Help & Support Dialog
     */
    private void showHelpDialog() {
        JDialog dialog = new JDialog(this, "Help & Support", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(WHITE);
        
        JLabel titleLabel = new JLabel("Help & Support");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(HEADER_BLUE);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        
        String[] helpText = {
            "Helpline: 1800-XXX-XXXX",
            "Email: support@mcc.neet.gov.in",
            "Technical Support: tech@mcc.neet.gov.in",
            "",
            "Office Hours:",
            "Monday - Friday: 9:00 AM - 6:00 PM",
            "Saturday: 9:00 AM - 1:00 PM",
            "Sunday: Closed",
            "",
            "For urgent issues, please contact our helpline.",
            "Response time: Within 24 hours on working days."
        };
        
        for (String text : helpText) {
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            textLabel.setForeground(TEXT_PRIMARY);
            panel.add(textLabel);
            panel.add(Box.createVerticalStrut(8));
        }
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Auto-detect user type and login to appropriate dashboard
     * Checks Student -> College -> Admin in sequence
     */
    private void handleAutoLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Try Student login first
            if (tryStudentLogin(username, password)) {
                return;
            }
            
            // Try College login next
            if (tryCollegeLogin(username, password)) {
                return;
            }
            
            // Try Admin login last
            if (tryAdminLogin(username, password)) {
                return;
            }
            
            // If all failed, show invalid credentials
            JOptionPane.showMessageDialog(this,
                "Invalid username or password!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Login error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Try to authenticate as Student
     * Returns true if successful, false otherwise
     */
    private boolean tryStudentLogin(String username, String password) {
        try {
            com.neet.dao.StudentDAO studentDAO = new com.neet.dao.StudentDAO();
            com.neet.model.Student student = studentDAO.authenticateStudent(username, password);
            
            if (student != null) {
                this.dispose();
                StudentDashboard dashboard = new StudentDashboard(student);
                dashboard.setVisible(true);
                return true;
            }
        } catch (Exception ex) {
            // Silently fail and try next user type
        }
        return false;
    }
    
    /**
     * Try to authenticate as College
     * Returns true if successful, false otherwise
     */
    private boolean tryCollegeLogin(String username, String password) {
        String sql = "SELECT COLLEGE_ID, COLLEGE_NAME FROM colleges WHERE USERNAME = ? AND PASSWORD = ?";
        
        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int collegeId = rs.getInt("COLLEGE_ID");
                String collegeName = rs.getString("COLLEGE_NAME");
                
                this.dispose();
                new CollegeDashboard(collegeId, collegeName).setVisible(true);
                return true;
            }
        } catch (Exception ex) {
            // Silently fail and try next user type
        }
        return false;
    }
    
    /**
     * Try to authenticate as Admin
     * Returns true if successful, false otherwise
     */
    private boolean tryAdminLogin(String username, String password) {
        String sql = "SELECT adminid, fullname FROM adminusers WHERE username = ? AND password = ?";
        
        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int adminId = rs.getInt("adminid");
                String adminName = rs.getString("fullname");
                
                this.dispose();
                new AdminDashboard(adminId, adminName).setVisible(true);
                return true;
            }
        } catch (Exception ex) {
            // Silently fail
        }
        return false;
    }
    
    /**
     * Handle registration - only for students
     */
    private void handleRegister() {
        this.dispose();
        new RegistrationFrame().setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}
