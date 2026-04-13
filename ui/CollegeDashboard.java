package com.neet.ui;

import com.neet.dao.*;
import com.neet.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * ✅ COMPLETE: College Dashboard - Modern UI (Same as Admin)
 * - Professional sidebar navigation
 * - Modern header with college info
 * - All college functionalities preserved
 * - 7 tabs with modern styling
 * - Professional color scheme
 * ✅ FIXED: Ready to Admit now updates database correctly
 */
public class CollegeDashboard extends JFrame {
    private int collegeId;
    private College college;
    private CollegeDAO collegeDAO;
    private StudentDAO studentDAO;
    private JPanel contentPanel;
    private String currentMenu = "College Info";

    // Modern Color Palette (Same as Admin)
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private static final Color SIDEBAR_SELECTED = new Color(52, 73, 94);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);

    public CollegeDashboard(int collegeId, String collegeName) {
        this.collegeId = collegeId;
        this.collegeDAO = new CollegeDAO();
        this.studentDAO = new StudentDAO();
        this.college = collegeDAO.getCollegeById(collegeId);
        
        if (this.college == null) {
            this.college = new College();
            this.college.setCollegeId(collegeId);
            this.college.setCollegeName(collegeName);
        }

        setTitle("College Dashboard - " + college.getCollegeName());
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = createModernHeader();
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(BACKGROUND_COLOR);

        JPanel sidebarPanel = createSidebar();
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        loadContent("College Info");

        bodyPanel.add(sidebarPanel, BorderLayout.WEST);
        bodyPanel.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("🏫 College Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel(college.getCollegeName() + " (ID: " + collegeId + ")");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(subtitleLabel);

        JButton refreshButton = createStyledButton("🔄 Refresh", SECONDARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> refreshAllTabs());

        JButton logoutButton = createStyledButton("🚪 Logout", ACCENT_COLOR, Color.WHITE);
        logoutButton.addActionListener(e -> {
            this.dispose();
            new HomePage().setVisible(true);
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(refreshButton);
        rightPanel.add(logoutButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[] menuItems = {
            "College Info",
            "Courses",
            "Provisional Allotments",
            "Document Verification",
            "Payment Verification",
            "Ready to Admit",
            "Admitted Students"
        };

        String[] menuIcons = {
            "ℹ️",
            "📚",
            "✅",
            "📄",
            "💰",
            "✔️",
            "👥"
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuIcons[i] + " " + menuItems[i], menuItems[i]);
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(5));
        }

        return sidebar;
    }

    private JButton createMenuButton(String text, String menuName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(currentMenu.equals(menuName) ? SIDEBAR_SELECTED : SIDEBAR_COLOR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(280, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!currentMenu.equals(menuName)) {
                    button.setBackground(SIDEBAR_SELECTED);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!currentMenu.equals(menuName)) {
                    button.setBackground(SIDEBAR_COLOR);
                }
            }
        });

        button.addActionListener(e -> {
            currentMenu = menuName;
            loadContent(menuName);
        });

        return button;
    }

    private void loadContent(String menuName) {
        contentPanel.removeAll();
        
        switch (menuName) {
            case "College Info":
                contentPanel.add(createCollegeInfoPanel(), BorderLayout.CENTER);
                break;
            case "Courses":
                contentPanel.add(createCoursesPanel(), BorderLayout.CENTER);
                break;
            case "Provisional Allotments":
                contentPanel.add(createAllottedStudentsPanel(), BorderLayout.CENTER);
                break;
            case "Document Verification":
                contentPanel.add(createDocumentVerificationPanel(), BorderLayout.CENTER);
                break;
            case "Payment Verification":
                contentPanel.add(createPaymentVerificationPanel(), BorderLayout.CENTER);
                break;
            case "Ready to Admit":
                contentPanel.add(createReadyToAdmitPanel(), BorderLayout.CENTER);
                break;
            case "Admitted Students":
                contentPanel.add(createAdmittedStudentsPanel(), BorderLayout.CENTER);
                break;
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ===== CONTENT PANELS =====

    private JPanel createCollegeInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel headerLabel = new JLabel("ℹ️ College Information");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(TEXT_PRIMARY);
        panel.add(headerLabel);
        panel.add(Box.createVerticalStrut(20));

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 30, 20));
        infoPanel.setBackground(BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addInfoRow(infoPanel, "College ID:", String.valueOf(college.getCollegeId()));
        addInfoRow(infoPanel, "College Name:", college.getCollegeName());
        addInfoRow(infoPanel, "College Type:", college.getCollegeType() != null ? college.getCollegeType() : "N/A");
        addInfoRow(infoPanel, "State:", college.getState());
        addInfoRow(infoPanel, "City:", college.getCity());
        addInfoRow(infoPanel, "Email:", college.getEmail());
        addInfoRow(infoPanel, "Phone:", college.getPhone());
        addInfoRow(infoPanel, "Total Seats:", String.valueOf(college.getTotalSeats()));
        addInfoRow(infoPanel, "Status:", college.getStatus());

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        panel.add(scrollPane);
        return panel;
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(SECONDARY_COLOR);
        
        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComp.setForeground(TEXT_PRIMARY);
        
        panel.add(labelComp);
        panel.add(valueComp);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("📚 Courses Offered");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"Course ID", "Course Name", "Type", "Seats", "Fees/Year"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT course_id, course_name, course_type, available_seats, fees_per_year " +
                        "FROM courses WHERE college_id = " + collegeId;
            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getString("course_type"),
                    rs.getInt("available_seats"),
                    String.format("₹ %.0f", rs.getDouble("fees_per_year"))
                });
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }

        JTable table = new JTable(model);
        styleModernTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> loadContent("Courses"));
        buttonPanel.add(refreshButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAllottedStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("✅ Provisional Allotments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"Allot ID", "Student ID", "Name", "NEET Rank", "Course", "Doc Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT sa.allotment_id, sa.student_id, s.full_name, s.neet_rank, " +
                        "c.course_name, NVL(sa.document_verification_status, 'PENDING') as doc_status " +
                        "FROM student_allotment sa " +
                        "JOIN students s ON sa.student_id = s.student_id " +
                        "JOIN courses c ON sa.course_id = c.course_id " +
                        "WHERE sa.college_id = " + collegeId + " AND sa.acceptance_status = 'ACCEPTED'";
            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("allotment_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getInt("neet_rank"),
                    rs.getString("course_name"),
                    rs.getString("doc_status")
                });
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error loading allotted students: " + e.getMessage());
        }

        JTable table = new JTable(model);
        styleModernTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> loadContent("Provisional Allotments"));
        buttonPanel.add(refreshButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDocumentVerificationPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("📄 Document Verification");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"Allot ID", "Student ID", "Name", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT sa.allotment_id, sa.student_id, s.full_name, " +
                        "NVL(sa.document_verification_status, 'PENDING') as status " +
                        "FROM student_allotment sa " +
                        "JOIN students s ON sa.student_id = s.student_id " +
                        "WHERE sa.college_id = " + collegeId + " AND sa.acceptance_status = 'ACCEPTED'";
            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("allotment_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("status")
                });
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error loading document verification: " + e.getMessage());
        }

        JTable table = new JTable(model);
        styleModernTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton verifyButton = createStyledButton("✓ Verify Documents", SECONDARY_COLOR, Color.WHITE);
        verifyButton.addActionListener(e -> handleVerifyDocuments(table, model));
        buttonPanel.add(verifyButton);
     // ✅ ADD THIS BUTTON - VIEW DOCUMENTS
        JButton viewDocsButton = createStyledButton("📄 View Documents", new Color(52, 152, 219), Color.WHITE);
        viewDocsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Please select a student first",
                    "Selection Required", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int studentId = (Integer) table.getValueAt(selectedRow, 1);
            String studentName = (String) table.getValueAt(selectedRow, 2);
            
            // Show document viewer dialog
            showStudentDocumentsDialog(studentId, studentName);
        });
        buttonPanel.add(viewDocsButton);


        JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> loadContent("Document Verification"));
        buttonPanel.add(refreshButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleVerifyDocuments(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a student to verify");
            return;
        }

        int studentId = (Integer) table.getValueAt(selectedRow, 1);
        String studentName = (String) table.getValueAt(selectedRow, 2);
        
        JPanel checklistPanel = new JPanel();
        checklistPanel.setLayout(new BoxLayout(checklistPanel, BoxLayout.Y_AXIS));
        checklistPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("✅ VERIFY DOCUMENTS FOR: " + studentName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checklistPanel.add(titleLabel);
        checklistPanel.add(Box.createVerticalStrut(15));
        
        JCheckBox check10 = new JCheckBox("✓ 10th Marksheet Verified");
        JCheckBox check12 = new JCheckBox("✓ 12th Marksheet Verified");
        JCheckBox checkNeet = new JCheckBox("✓ NEET Score Card Verified");
        JCheckBox checkAadhar = new JCheckBox("✓ Aadhar/ID Verified");
        
        checklistPanel.add(check10);
        checklistPanel.add(check12);
        checklistPanel.add(checkNeet);
        checklistPanel.add(checkAadhar);
        
        int result = JOptionPane.showConfirmDialog(this, checklistPanel, 
            "Verify Documents", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            if (!check10.isSelected() || !check12.isSelected() || !checkNeet.isSelected() || !checkAadhar.isSelected()) {
                JOptionPane.showMessageDialog(this, "Please check all documents!", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                String updateSQL = "UPDATE student_allotment SET document_verification_status = 'DOCUMENTS_VERIFIED', " +
                                  "verification_date = SYSDATE WHERE student_id = ? AND college_id = ? AND acceptance_status = 'ACCEPTED'";
                
                try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
                     java.sql.PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, studentId);
                    pstmt.setInt(2, collegeId);
                    
                    int updated = pstmt.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, 
                            "✅ Documents verified!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadContent("Document Verification");
                    }
                }
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createPaymentVerificationPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("💰 Payment Verification");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"Payment ID", "Student ID", "Name", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
        	String sql = "SELECT sp.payment_id, sp.student_id, s.full_name, " +
        		    "sp.amount_due, sp.payment_status " +
        		    "FROM student_payments sp " +
        		    "JOIN students s ON sp.student_id = s.student_id " +
        		    "JOIN student_allotment sa ON sp.student_id = sa.student_id AND sp.college_id = sa.college_id " +
        		    "WHERE sp.college_id = " + collegeId + " " +
        		    "AND sa.acceptance_status = 'ACCEPTED' " +
        		    "ORDER BY sp.payment_status ASC, sp.payment_id DESC";

            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("payment_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    String.format("₹ %.2f", rs.getDouble("amount_due")),
                    rs.getString("payment_status")
                });
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error loading payments: " + e.getMessage());
        }

        JTable table = new JTable(model);
        styleModernTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton markPaidButton = createStyledButton("✓ Mark as Paid", SECONDARY_COLOR, Color.WHITE);
        markPaidButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a payment to mark as paid");
                return;
            }
            
            int paymentId = (Integer) table.getValueAt(selectedRow, 0);
            String currentStatus = (String) table.getValueAt(selectedRow, 4);
            
            if ("PAID".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Payment already marked as PAID!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                String updateSQL = "UPDATE student_payments SET payment_status = 'PAID', payment_date = SYSDATE " +
                                  "WHERE payment_id = ?";
                
                try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
                     java.sql.PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setInt(1, paymentId);
                    
                    int updated = pstmt.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, "✓ Payment marked as PAID!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadContent("Payment Verification");
                    }
                }
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(markPaidButton);

        JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> loadContent("Payment Verification"));
        buttonPanel.add(refreshButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ✅✅✅ FIXED METHOD - NOW UPDATES DATABASE ✅✅✅
    private JPanel createReadyToAdmitPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("✔️ Ready to Admit (All Conditions Met)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"Student ID", "Name", "Course", "Docs", "Payment", "Acceptance"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT sa.student_id, s.full_name, c.course_name, " +
                        "sa.document_verification_status, sp.payment_status, sa.acceptance_status " +
                        "FROM student_allotment sa " +
                        "JOIN students s ON sa.student_id = s.student_id " +
                        "JOIN courses c ON sa.course_id = c.course_id " +
                        "LEFT JOIN student_payments sp ON sa.allotment_id = sp.allotment_id " +
                        "WHERE sa.college_id = " + collegeId + " " +
                        "AND sa.acceptance_status = 'ACCEPTED' " +
                        "AND sa.document_verification_status LIKE '%VERIFIED%' " +
                        "AND COALESCE(sp.payment_status, 'PAID') = 'PAID' " +
                        "AND (sa.admission_status IS NULL OR sa.admission_status != 'ADMITTED') " +
                        "ORDER BY sa.student_id";
            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("course_name"),
                    rs.getString("document_verification_status"),
                    rs.getString("payment_status"),
                    rs.getString("acceptance_status")
                });
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error loading ready to admit: " + e.getMessage());
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        styleModernTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton admitButton = createStyledButton("✅ Admit Selected", SECONDARY_COLOR, Color.WHITE);
        admitButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a student to admit");
                return;
            }

            int studentId = (Integer) table.getValueAt(selectedRow, 0);
            String studentName = (String) table.getValueAt(selectedRow, 1);
            String courseName = (String) table.getValueAt(selectedRow, 2);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Admit " + studentName + " to " + courseName + "?\n\n" +
                "This will officially admit the student.",
                "Confirm Admission",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // ✅ UPDATE DATABASE WITH ADMISSION STATUS
                    String updateSQL = 
                        "UPDATE student_allotment " +
                        "SET admission_status = 'ADMITTED', admission_date = SYSDATE " +
                        "WHERE student_id = ? AND college_id = ? AND acceptance_status = 'ACCEPTED'";
                    
                    try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
                         java.sql.PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                        
                        pstmt.setInt(1, studentId);
                        pstmt.setInt(2, collegeId);
                        int updated = pstmt.executeUpdate();
                        
                        if (updated > 0) {
                            JOptionPane.showMessageDialog(this,
                                "✅ " + studentName + " admitted successfully!\n" +
                                "Admission ID: ADM-" + collegeId + "-" + studentId,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                            loadContent("Ready to Admit");
                        } else {
                            JOptionPane.showMessageDialog(this,
                                "Failed to admit student.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(admitButton);

        JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> loadContent("Ready to Admit"));
        buttonPanel.add(refreshButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAdmittedStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("👥 Admitted Students (Fee Paid)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] columnNames = {"Student ID", "Name", "Course", "Fee Paid", "Payment Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int totalStudents = 0;
        double totalFees = 0;
        
        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT sp.student_id, s.full_name, c.course_name, " +
                        "sp.amount_due, sp.payment_date " +
                        "FROM student_payments sp " +
                        "JOIN students s ON sp.student_id = s.student_id " +
                        "JOIN courses c ON sp.course_id = c.course_id " +
                        "WHERE sp.college_id = " + collegeId + " " +
                        "AND sp.payment_status = 'PAID' " +
                        "ORDER BY sp.payment_date DESC";
            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                double amount = rs.getDouble("amount_due");
                java.sql.Date paymentDate = rs.getDate("payment_date");
                
                model.addRow(new Object[]{
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("course_name"),
                    String.format("₹ %.0f", amount),
                    paymentDate != null ? new java.text.SimpleDateFormat("dd-MM-yyyy").format(paymentDate) : "N/A"
                });
                
                totalStudents++;
                totalFees += amount;
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error loading admitted students: " + e.getMessage());
        }

        JTable table = new JTable(model);
        styleModernTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        statsPanel.setBackground(new Color(245, 245, 245));
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel totalLabel = new JLabel("Total Admitted: " + totalStudents);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLabel.setForeground(TEXT_PRIMARY);

        JLabel feesLabel = new JLabel("Fees Collected: ₹" + String.format("%.0f", totalFees));
        feesLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feesLabel.setForeground(SECONDARY_COLOR);

        statsPanel.add(totalLabel);
        statsPanel.add(feesLabel);

        JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> loadContent("Admitted Students"));
        statsPanel.add(refreshButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void styleModernTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(52, 152, 219, 80));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(189, 195, 199));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setOpaque(true);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(189, 195, 199));
                c.setForeground(Color.BLACK);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setBackground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    private void refreshAllTabs() {
        try {
            college = collegeDAO.getCollegeById(collegeId);
            loadContent(currentMenu);
            JOptionPane.showMessageDialog(this, "✓ All data refreshed successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    /**
     * Show dialog with student's uploaded documents
     * Allows college to view PDFs, images, and other documents
     */
    private void showStudentDocumentsDialog(int studentId, String studentName) {
        // Create dialog
        JDialog docDialog = new JDialog(this, "📄 View Documents - " + studentName, true);
        docDialog.setSize(900, 600);
        docDialog.setLocationRelativeTo(this);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel headerLabel = new JLabel("📄 Student Documents - " + studentName + " (ID: " + studentId + ")");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(TEXT_PRIMARY);
        
        // Table with documents
        String[] columnNames = {"Doc Type", "File Name", "Upload Date", "File Size", "Action"};
        DefaultTableModel docModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable docTable = new JTable(docModel);
        styleModernTable(docTable);
        docTable.setRowHeight(40);
        
        // Fetch documents from database
        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT document_type, file_path, file_size, upload_date " +
                        "FROM student_documents " +
                        "WHERE student_id = " + studentId + " " +
                        "ORDER BY document_type";
            
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String docType = rs.getString("document_type");
                String filePath = rs.getString("file_path");
                java.sql.Timestamp uploadDate = rs.getTimestamp("upload_date");
                long fileSize = rs.getLong("file_size");
                
                // Extract filename from path
                String fileName = filePath != null ? new java.io.File(filePath).getName() : "N/A";
                
                // Format file size
                String sizeStr = formatFileSize(fileSize);
                
                // Format date
                String dateStr = uploadDate != null ? 
                    new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(uploadDate) : "N/A";
                
                docModel.addRow(new Object[]{
                    docType,
                    fileName,
                    dateStr,
                    sizeStr,
                    "View"
                });
            }
            
            if (docModel.getRowCount() == 0) {
                docModel.addRow(new Object[]{"No documents found", "-", "-", "-", "-"});
            }
            
        } catch (java.sql.SQLException e) {
            System.out.println("❌ Error loading documents: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(docDialog,
                "Error loading documents: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        JScrollPane docScroll = new JScrollPane(docTable);
        docScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        // View Selected Document button
        JButton viewDocButton = createStyledButton("👁️ View Selected Document", PRIMARY_COLOR, Color.WHITE);
        viewDocButton.addActionListener(e -> {
            int selectedRow = docTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(docDialog,
                    "⚠️ Please select a document to view",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String docType = (String) docModel.getValueAt(selectedRow, 0);
            
            if ("No documents found".equals(docType)) {
                return;
            }
            
            // Fetch file path from database
            try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
                 java.sql.PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT file_path FROM student_documents WHERE student_id = ? AND document_type = ?")) {
                
                pstmt.setInt(1, studentId);
                pstmt.setString(2, docType);
                java.sql.ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    String filePath = rs.getString("file_path");
                    
                    if (filePath != null && !filePath.isEmpty()) {
                        // ✅ USE DocumentViewerDialog with JDialog parent
                        new DocumentViewerDialog(docDialog, filePath).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(docDialog,
                            "❌ File path is empty or null",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(docDialog,
                        "❌ Document not found in database",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (java.sql.SQLException ex) {
                System.out.println("❌ Error fetching document: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(docDialog,
                    "Error fetching document: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Close button
        JButton closeButton = createStyledButton("Close", ACCENT_COLOR, Color.WHITE);
        closeButton.addActionListener(e -> docDialog.dispose());
        
        buttonPanel.add(viewDocButton);
        buttonPanel.add(closeButton);
        
        // Add components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(docScroll, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        docDialog.add(mainPanel);
        docDialog.setVisible(true);
    }
    /**
     * Format file size in human-readable format
     */
    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }


}
