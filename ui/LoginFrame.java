package com.neet.ui;

import com.neet.dao.*;
import com.neet.model.*;
import com.neet.db.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JButton loginButton;
    private JButton registerButton;
    
    // Modern Colors
    private static final Color DARK_NAVY = new Color(30, 40, 75);      // Dark navy background
    private static final Color CYAN_ACCENT = new Color(0, 200, 200);   // Cyan buttons
    private static final Color INPUT_BG = new Color(50, 70, 110);      // Input field background
    private static final Color TEXT_LIGHT = new Color(200, 210, 220);  // Light text
    
    public LoginFrame() {
        setTitle("NEET Counselling - Secure Login");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                    0, 0, DARK_NAVY,
                    0, getHeight(), new Color(40, 55, 100)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        JPanel formCard = createFormCard();
        
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.add(formCard, gbc);
        
        add(mainPanel);
    }
    
    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(35, 50, 90));
        card.setBorder(BorderFactory.createLineBorder(CYAN_ACCENT, 2));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(420, 650));
        
        // Icon at top - USER PROFILE
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setForeground(CYAN_ACCENT);
        iconPanel.add(iconLabel);
        card.add(iconPanel);
        
        card.add(Box.createVerticalStrut(25));
        
        // Sign In Title
        JLabel signInLabel = new JLabel("SIGN IN");
        signInLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        signInLabel.setForeground(CYAN_ACCENT);
        signInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(signInLabel);
        
        card.add(Box.createVerticalStrut(35));
        
        // Login As Label
        JLabel typeLabel = new JLabel("Login As");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(TEXT_LIGHT);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(typeLabel);
        
        card.add(Box.createVerticalStrut(8));
        
        // Combo Box
        userTypeCombo = createStyledComboBox();
        userTypeCombo.setMaximumSize(new Dimension(340, 45));
        card.add(userTypeCombo);
        
        card.add(Box.createVerticalStrut(20));
        
        // Username Field with Icon
        JPanel usernamePanel = createInputFieldWithIcon("👤", "Username");
        usernameField = (JTextField) usernamePanel.getClientProperty("field");
        card.add(usernamePanel);
        
        card.add(Box.createVerticalStrut(18));
        
        // Password Field with Icon
        JPanel passwordPanel = createPasswordFieldWithIcon("🔒", "Password");
        passwordField = (JPasswordField) passwordPanel.getClientProperty("field");
        card.add(passwordPanel);
        
        card.add(Box.createVerticalStrut(25));
        
        // Login Button - CYAN
        loginButton = createModernButton("LOGIN", CYAN_ACCENT, DARK_NAVY);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.setMaximumSize(new Dimension(340, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginButton);
        
        card.add(Box.createVerticalStrut(12));
        
        // Register Button
        registerButton = createModernButton("CREATE ACCOUNT", new Color(100, 150, 180), DARK_NAVY);
        registerButton.addActionListener(e -> handleRegister());
        registerButton.setMaximumSize(new Dimension(340, 45));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(registerButton);
        
        card.add(Box.createVerticalStrut(20));
        
        // Footer
        JLabel footerLabel = new JLabel("© 2025 NEET Counselling System");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLabel.setForeground(new Color(120, 130, 150));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footerLabel);
        
        if (passwordField != null) {
            passwordField.addActionListener(e -> handleLogin());
        }
        
        return card;
    }
    
    private JPanel createInputFieldWithIcon(String icon, String placeholder) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(INPUT_BG);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        fieldPanel.setMaximumSize(new Dimension(340, 45));
        fieldPanel.setPreferredSize(new Dimension(340, 45));
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(CYAN_ACCENT);
        
        // Input field - NO PLACEHOLDER
        usernameField = new JTextField();  // ✅ Empty field, no placeholder
        usernameField.setBackground(INPUT_BG);
        usernameField.setForeground(TEXT_LIGHT);
        usernameField.setCaretColor(CYAN_ACCENT);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        fieldPanel.add(iconLabel, BorderLayout.WEST);
        fieldPanel.add(usernameField, BorderLayout.CENTER);
        fieldPanel.putClientProperty("field", usernameField);
        
        return fieldPanel;
    }

    private JPanel createPasswordFieldWithIcon(String icon, String placeholder) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(INPUT_BG);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        fieldPanel.setMaximumSize(new Dimension(340, 45));
        fieldPanel.setPreferredSize(new Dimension(340, 45));
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(CYAN_ACCENT);
        
        // Password field - NO PLACEHOLDER
        passwordField = new JPasswordField();  // ✅ Empty field, no placeholder
        passwordField.setBackground(INPUT_BG);
        passwordField.setForeground(TEXT_LIGHT);
        passwordField.setCaretColor(CYAN_ACCENT);
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        fieldPanel.add(iconLabel, BorderLayout.WEST);
        fieldPanel.add(passwordField, BorderLayout.CENTER);
        fieldPanel.putClientProperty("field", passwordField);
        
        return fieldPanel;
    }

    private JComboBox<String> createStyledComboBox() {
        String[] userTypes = {"Student", "College", "Admin"};
        JComboBox<String> combo = new JComboBox<>(userTypes);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(INPUT_BG);
        combo.setForeground(Color.BLACK);  // ✅ CHANGED TO BLACK
        combo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        combo.setFocusable(true);
        
        return combo;
    }

    
    private JButton createModernButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(340, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            private Color originalBg = bgColor;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
                button.repaint();
            }
        });
        
        return button;
    }

    private void handleLogin() {
        String userType = (String) userTypeCombo.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showStyledMessage("Please enter both username and password!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (userType.equals("Student")) {
                handleStudentLogin(username, password);
            } else if (userType.equals("College")) {
                handleCollegeLogin(username, password);
            } else if (userType.equals("Admin")) {
                handleAdminLogin(username, password);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessage("Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdminLogin(String username, String password) {
        String sql = "SELECT admin_id, full_name FROM admin_users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int adminId = rs.getInt("admin_id");
                String adminName = rs.getString("full_name");
                this.dispose();
                new AdminDashboard(adminId, adminName).setVisible(true);
            } else {
                showStyledMessage("Invalid admin credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            showStyledMessage("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCollegeLogin(String username, String password) {
        String sql = "SELECT college_id, college_name FROM colleges WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int collegeId = rs.getInt("college_id");
                String collegeName = rs.getString("college_name");
                this.dispose();
                new CollegeDashboard(collegeId, collegeName).setVisible(true);
            } else {
                showStyledMessage("Invalid college credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                usernameField.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessage("Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleStudentLogin(String username, String password) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            Student student = studentDAO.authenticateStudent(username, password);
            
            if (student != null) {
                this.dispose();
                StudentDashboard dashboard = new StudentDashboard(student);
                dashboard.setVisible(true);
            } else {
                showStyledMessage("Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                usernameField.requestFocus();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledMessage("Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String userType = (String) userTypeCombo.getSelectedItem();
        
        if (userType.equals("Student")) {
            this.dispose();
            new RegistrationFrame().setVisible(true);
        } else {
            showStyledMessage(
                "Registration is only available for students.\n" +
                "Colleges and Admins are registered by the system administrator.", 
                "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", new Color(50, 70, 110));
        UIManager.put("Panel.background", new Color(50, 70, 110));
        UIManager.put("OptionPane.messageForeground", TEXT_LIGHT);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
