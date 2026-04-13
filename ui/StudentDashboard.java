package com.neet.ui;

import com.neet.dao.*;
import com.neet.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Map;
import java.util.List;

/**
 * ✅ COMPLETE FIXED StudentDashboard.java
 * - Allotment Status with ACCEPT/DECLINE buttons ✅
 * - WAITLIST message display ✅
 * - Download button ONLY when ADMITTED ✅
 * - Delete Choice button in My Choices ✅
 * - Payment Status with proper database queries
 * - All features working correctly
 */
public class StudentDashboard extends JFrame {
    private Student student;
    private int studentId;
    private JPanel contentPanel;
    private String currentMenu = "Profile";

    // Modern Color Palette
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
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);

    public StudentDashboard(Student student) {
        this.student = student;
        this.studentId = student.getStudentId();
        setTitle("Student Dashboard - " + student.getFullName());
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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

        loadContent("Profile");

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

        JLabel titleLabel = new JLabel("🎓 Student Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Welcome, " + student.getFullName());
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(subtitleLabel);

        JButton logoutButton = createStyledButton("🚪 Logout", ACCENT_COLOR, Color.WHITE);
        logoutButton.addActionListener(e -> {
            this.dispose();
            new HomePage().setVisible(true);
        });

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[] menuItems = {"Profile", "Choice Filling", "Allotment Status", "My Choices", "💳 Payment Status"};
        String[] menuIcons = {"👤", "📋", "🎯", "✓", "💳"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuIcons[i] + " " + menuItems[i], menuItems[i]);
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(5));
        }

        sidebar.add(Box.createVerticalGlue());
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
        button.setMaximumSize(new Dimension(250, 50));
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
            reloadStudentData();
            loadContent(menuName);
            refreshSidebar();
        });

        return button;
    }

    private void refreshSidebar() {
    }

    private void reloadStudentData() {
        StudentDAO dao = new StudentDAO();
        Student updatedStudent = dao.getStudentById(student.getStudentId());
        if (updatedStudent != null) {
            this.student = updatedStudent;
        }
    }

    private void loadContent(String menuName) {
        contentPanel.removeAll();
        switch (menuName) {
            case "Profile":
                contentPanel.add(createProfilePanel(), BorderLayout.CENTER);
                break;
            case "Choice Filling":
                contentPanel.add(createChoiceFillingPanel(), BorderLayout.CENTER);
                break;
            case "Allotment Status":
                contentPanel.add(createAllotmentStatusPanel(), BorderLayout.CENTER);
                break;
            case "My Choices":
                contentPanel.add(createMyChoicesPanel(), BorderLayout.CENTER);
                break;
            case "💳 Payment Status":
                contentPanel.add(createPaymentStatusPanel(), BorderLayout.CENTER);
                break;
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ===== PROFILE PANEL =====
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        JLabel profileTitle = new JLabel("Your Profile Information");
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        profileTitle.setForeground(TEXT_PRIMARY);
        panel.add(profileTitle, gbc);
        gbc.gridwidth = 1;
        row++;

        addSectionHeader(panel, gbc, row++, "Personal Information");
        addProfileField(panel, gbc, row++, "Student ID:", String.valueOf(student.getStudentId()));
        addProfileField(panel, gbc, row++, "Full Name:", student.getFullName());
        addProfileField(panel, gbc, row++, "Gender:", student.getGender());

        addSectionHeader(panel, gbc, row++, "Contact Information");
        addProfileField(panel, gbc, row++, "Email:", student.getEmail());
        addProfileField(panel, gbc, row++, "Mobile Number:", student.getMobileNumber());

        addSectionHeader(panel, gbc, row++, "NEET Score Details");
        addProfileField(panel, gbc, row++, "NEET Score:", String.valueOf(student.getNeetScore()));
        addProfileField(panel, gbc, row++, "NEET Rank (AIR):", String.valueOf(student.getNeetRank()));
        addProfileField(panel, gbc, row++, "Category:", student.getCategory());

        addSectionHeader(panel, gbc, row++, "Application Status");
        addProfileField(panel, gbc, row++, "Registration Status:", student.getRegistrationStatus());
        addProfileField(panel, gbc, row++, "Choice Locked:", student.getChoiceLocked());
        addProfileField(panel, gbc, row++, "Allotment Status:", student.getAllotmentStatus());

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(BACKGROUND_COLOR);
        wrapperPanel.add(panel, BorderLayout.NORTH);

        return wrapperPanel;
    }

    private void addSectionHeader(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 8, 10);

        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(PRIMARY_COLOR);
        panel.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
    }

    private void addProfileField(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComp.setForeground(TEXT_SECONDARY);
        panel.add(labelComp, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;

        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueComp.setForeground(TEXT_PRIMARY);
        panel.add(valueComp, gbc);
    }

    // ===== CHOICE FILLING PANEL =====
    private JPanel createChoiceFillingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(BACKGROUND_COLOR);

        if (student.getRegistrationStatus() == null || !student.getRegistrationStatus().equals("VERIFIED")) {
            JLabel msgLabel = new JLabel("⏳ Your registration is pending verification. Choice filling will be enabled after verification.");
            msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            msgLabel.setForeground(TEXT_PRIMARY);
            panel.add(msgLabel, BorderLayout.CENTER);
            return panel;
        }

        if (student.getChoiceLocked() != null && student.getChoiceLocked().equals("YES")) {
            JLabel msgLabel = new JLabel("✓ Your choices have been locked. You cannot modify them now.");
            msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            msgLabel.setForeground(SECONDARY_COLOR);
            panel.add(msgLabel, BorderLayout.CENTER);
            return panel;
        }

        JButton openSearchBtn = createStyledButton("🔍 Open College Search & Choice Filling", PRIMARY_COLOR, Color.WHITE);
        openSearchBtn.setPreferredSize(new Dimension(400, 60));
        openSearchBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        openSearchBtn.addActionListener(e -> {
            new StudentChoiceFrame(student.getStudentId()).setVisible(true);
        });

        JLabel infoLabel = new JLabel("Search for colleges, view available courses, and fill your choices in the popup.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoLabel.setForeground(TEXT_SECONDARY);

        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(openSearchBtn, BorderLayout.CENTER);

        return panel;
    }

    // ===== ALLOTMENT STATUS PANEL =====
    private JPanel createAllotmentStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("📋 Your Allotment Status");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);

        AllotmentDAO allotmentDAO = new AllotmentDAO();
        Map allotment = allotmentDAO.getStudentAllotment(student.getStudentId());

        String admissionStatus = getStudentAdmissionStatus(student.getStudentId());
        String acceptanceStatus = getStudentAcceptanceStatus(student.getStudentId());

        StudentDAO studentDAO = new StudentDAO();
        Student latestStudent = studentDAO.getStudentById(student.getStudentId());

        if (latestStudent != null && "WAITLIST".equals(latestStudent.getAllotmentStatus())) {
            JPanel waitlistPanel = new JPanel();
            waitlistPanel.setBackground(BACKGROUND_COLOR);
            waitlistPanel.setLayout(new BoxLayout(waitlistPanel, BoxLayout.Y_AXIS));

            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setBackground(CARD_COLOR);
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));

            JLabel statusLabel = new JLabel("⏳ You are in WAITLIST");
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            statusLabel.setForeground(new Color(245, 127, 23));
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel messageLabel = new JLabel(
                "<html>You did not get a seat in the allotted round.<br>" +
                "Your preferences will be reconsidered in the next round.<br><br>" +
                "🔓 Admin has unlocked your choices for the next round.<br>" +
                "You can now modify your college preferences.</html>"
            );
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            messageLabel.setForeground(TEXT_PRIMARY);
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            cardPanel.add(statusLabel);
            cardPanel.add(Box.createVerticalStrut(20));
            cardPanel.add(messageLabel);

            waitlistPanel.add(Box.createVerticalGlue());
            waitlistPanel.add(cardPanel);
            waitlistPanel.add(Box.createVerticalGlue());

            contentPanel.add(waitlistPanel, BorderLayout.CENTER);

        } else if (allotment.isEmpty()) {
            JPanel noAllotmentPanel = new JPanel();
            noAllotmentPanel.setBackground(BACKGROUND_COLOR);
            noAllotmentPanel.setLayout(new BoxLayout(noAllotmentPanel, BoxLayout.Y_AXIS));

            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setBackground(CARD_COLOR);
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));

            JLabel statusLabel = new JLabel("⏳ Allotment Not Yet Done");
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            statusLabel.setForeground(WARNING_COLOR);
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel messageLabel = new JLabel("Your allotment is being processed. Check back later for your results.");
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            messageLabel.setForeground(TEXT_PRIMARY);
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            cardPanel.add(statusLabel);
            cardPanel.add(Box.createVerticalStrut(20));
            cardPanel.add(messageLabel);

            noAllotmentPanel.add(Box.createVerticalGlue());
            noAllotmentPanel.add(cardPanel);
            noAllotmentPanel.add(Box.createVerticalGlue());

            contentPanel.add(noAllotmentPanel, BorderLayout.CENTER);

        } else {
            JPanel allotmentPanel = createAllotmentDetailsPanel(allotment, admissionStatus, acceptanceStatus);
            contentPanel.add(allotmentPanel, BorderLayout.CENTER);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshBtn = createStyledButton("🔄 Refresh Status", PRIMARY_COLOR, Color.WHITE);
        refreshBtn.addActionListener(e -> loadContent("Allotment Status"));

        buttonPanel.add(refreshBtn);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String getStudentAdmissionStatus(int studentId) {
        try {
            java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
            String sql = "SELECT admission_status FROM student_allotment WHERE student_id = ? ORDER BY allotment_id DESC";
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            String status = null;
            if (rs.next()) {
                status = rs.getString("admission_status");
            }

            rs.close();
            pstmt.close();
            conn.close();

            return status != null ? status : "PENDING";
        } catch (Exception e) {
            System.err.println("Error fetching admission status: " + e.getMessage());
            return "PENDING";
        }
    }

    private String getStudentAcceptanceStatus(int studentId) {
        try {
            java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
            String sql = "SELECT acceptance_status FROM student_allotment WHERE student_id = ? ORDER BY allotment_id DESC";
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            String status = null;
            if (rs.next()) {
                status = rs.getString("acceptance_status");
            }

            rs.close();
            pstmt.close();
            conn.close();

            return status != null ? status : "PENDING";
        } catch (Exception e) {
            System.err.println("Error fetching acceptance status: " + e.getMessage());
            return "PENDING";
        }
    }

    private JPanel createAllotmentDetailsPanel(Map allotment, String admissionStatus, String acceptanceStatus) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JPanel statusCard = new JPanel();
        statusCard.setLayout(new BoxLayout(statusCard, BoxLayout.Y_AXIS));
        statusCard.setBackground(new Color(200, 230, 201));
        statusCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        statusCard.setMaximumSize(new Dimension(600, 180));
        statusCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusTitleLabel = new JLabel("✅ ALLOTMENT CONFIRMED");
        statusTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statusTitleLabel.setForeground(new Color(27, 94, 32));
        statusTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String college = allotment.getOrDefault("college", "N/A").toString();
        String course = allotment.getOrDefault("course", "N/A").toString();

        JLabel detailsLabel = new JLabel(
            "<html>College: " + college + "<br>" +
            "Course: " + course + "<br>" +
            "Status: " + acceptanceStatus + "</html>"
        );
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailsLabel.setForeground(TEXT_PRIMARY);
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusCard.add(statusTitleLabel);
        statusCard.add(Box.createVerticalStrut(15));
        statusCard.add(detailsLabel);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionsPanel.setBackground(BACKGROUND_COLOR);

        if ("PENDING".equals(acceptanceStatus)) {
            JButton acceptBtn = createStyledButton("✅ ACCEPT OFFER", SECONDARY_COLOR, Color.WHITE);
            acceptBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to ACCEPT this allotment?\n" +
                    "You will be admitted to the allocated college and course.",
                    "Confirm Acceptance", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    updateStudentAcceptance(student.getStudentId(), "ACCEPTED");
                }
            });

            JButton declineBtn = createStyledButton("❌ DECLINE OFFER", ACCENT_COLOR, Color.WHITE);
            declineBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to DECLINE this allotment?\n" +
                    "You can participate in the next round of counselling.",
                    "Confirm Decline", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    updateStudentAcceptance(student.getStudentId(), "DECLINED");
                }
            });

            actionsPanel.add(acceptBtn);
            actionsPanel.add(declineBtn);

        } else if ("ACCEPTED".equals(acceptanceStatus) && "ADMITTED".equals(admissionStatus)) {
            JLabel admittedLabel = new JLabel("✅ Status: ADMITTED - College has admitted you!");
            admittedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            admittedLabel.setForeground(SECONDARY_COLOR);

            JButton downloadBtn = createStyledButton("📄 Download Allotment Letter",
                new Color(155, 89, 182), Color.WHITE);
            downloadBtn.addActionListener(e -> generateAllotmentLetter());

            actionsPanel.add(admittedLabel);
            actionsPanel.add(downloadBtn);

        } else if ("ACCEPTED".equals(acceptanceStatus)) {
            JLabel processingLabel = new JLabel("✅ Status: ACCEPTED - College is processing...");
            processingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            processingLabel.setForeground(WARNING_COLOR);
            actionsPanel.add(processingLabel);

        } else if ("DECLINED".equals(acceptanceStatus)) {
            JLabel declinedLabel = new JLabel("❌ Status: DECLINED");
            declinedLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            declinedLabel.setForeground(ACCENT_COLOR);
            actionsPanel.add(declinedLabel);
        }

        panel.add(Box.createVerticalStrut(20));
        panel.add(statusCard);
        panel.add(Box.createVerticalStrut(25));
        panel.add(actionsPanel);

        return panel;
    }

    // ===== MY CHOICES PANEL - WITH DELETE BUTTON ✅ =====
 // ✅ COMPLETE createMyChoicesPanel() - FINAL FIX
 // Replace ENTIRE createMyChoicesPanel() method with this:

 private JPanel createMyChoicesPanel() {
     JPanel panel = new JPanel(new BorderLayout(15, 15));
     panel.setBackground(BACKGROUND_COLOR);

     JLabel titleLabel = new JLabel("Your College Preferences");
     titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
     titleLabel.setForeground(TEXT_PRIMARY);

     String[] columnNames = {"Preference", "College", "Course", "Course Type", "Fees/Year"};
     DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
             return false;
         }
     };

     // ✅ DECLARE choiceDAO ONCE HERE - DON'T REDECLARE!
     StudentChoiceDAO choiceDAO = new StudentChoiceDAO();
     List<Course> choices = (List<Course>) choiceDAO.getStudentChoices(student.getStudentId());

     if (choices.isEmpty()) {
         model.addRow(new Object[]{"—", "No choices added yet", "—", "—", "—"});
     } else {
         for (Course course : choices) {
             model.addRow(new Object[]{
                 course.getPreferenceOrder(),
                 course.getCollegeName(),
                 course.getCourseName(),
                 course.getCourseType(),
                 String.format("₹ %.2f", course.getFeesPerYear())
             });
         }
     }

     JTable table = new JTable(model);
     styleModernTable(table);
     JScrollPane scrollPane = new JScrollPane(table);
     scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

     JPanel bottomPanel = new JPanel(new BorderLayout());
     bottomPanel.setBackground(BACKGROUND_COLOR);

     JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
     buttonPanel.setBackground(BACKGROUND_COLOR);

     JButton refreshButton = createStyledButton("🔄 Refresh", PRIMARY_COLOR, Color.WHITE);
     refreshButton.addActionListener(e -> {
         reloadStudentData();
         loadContent("My Choices");
     });
     buttonPanel.add(refreshButton);

     JButton addChoiceButton = createStyledButton("➕ Add More Choices", SECONDARY_COLOR, Color.WHITE);
     addChoiceButton.addActionListener(e -> {
         if (student.getRegistrationStatus() == null || !student.getRegistrationStatus().equals("VERIFIED")) {
             JOptionPane.showMessageDialog(this,
                 "Your registration is pending verification.",
                 "Not Verified",
                 JOptionPane.WARNING_MESSAGE);
             return;
         }

         if (student.getChoiceLocked() != null && student.getChoiceLocked().equals("YES")) {
             JOptionPane.showMessageDialog(this,
                 "Your choices have been locked.",
                 "Locked",
                 JOptionPane.WARNING_MESSAGE);
         } else {
             new StudentChoiceFrame(student.getStudentId()).setVisible(true);
         }
     });
     buttonPanel.add(addChoiceButton);

     // ✅ DELETE BUTTON - USES EXISTING choiceDAO (NO NEW DECLARATION!)
     JButton deleteButton = createStyledButton("🗑️ Delete Choice", ACCENT_COLOR, Color.WHITE);
     deleteButton.addActionListener(e -> {
         // Check if choices are locked
         if (student.getChoiceLocked() != null && student.getChoiceLocked().equals("YES")) {
             JOptionPane.showMessageDialog(this,
                 "❌ Your choices have been locked!\n\nYou cannot delete choices once they are locked.",
                 "Choices Locked",
                 JOptionPane.WARNING_MESSAGE);
             return;
         }

         int selectedRow = table.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this,
                 "Please select a choice to delete",
                 "No Selection",
                 JOptionPane.WARNING_MESSAGE);
             return;
         }

         int preferenceOrder = (int) model.getValueAt(selectedRow, 0);
         String collegeName = (String) model.getValueAt(selectedRow, 1);
         
         int confirm = JOptionPane.showConfirmDialog(this,
             "Delete choice #" + preferenceOrder + " - " + collegeName + "?",
             "Confirm Delete",
             JOptionPane.YES_NO_OPTION);

         if (confirm == JOptionPane.YES_OPTION) {
             try {
                 // ✅ USE EXISTING choiceDAO - NO REDECLARATION!
                 choiceDAO.deleteChoice(student.getStudentId(), preferenceOrder);
                 
                 JOptionPane.showMessageDialog(this,
                     "✅ Choice deleted successfully!",
                     "Success",
                     JOptionPane.INFORMATION_MESSAGE);
                 
                 loadContent("My Choices");
             } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this,
                     "❌ Error deleting choice: " + ex.getMessage(),
                     "Error",
                     JOptionPane.ERROR_MESSAGE);
             }
         }
     });
     buttonPanel.add(deleteButton);

     bottomPanel.add(buttonPanel, BorderLayout.CENTER);

     panel.add(titleLabel, BorderLayout.NORTH);
     panel.add(scrollPane, BorderLayout.CENTER);
     panel.add(bottomPanel, BorderLayout.SOUTH);

     return panel;
 }




    // ===== PAYMENT STATUS PANEL =====
    private JPanel createPaymentStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("💳 Payment Status & Instructions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String sql = "SELECT sa.allotment_id, sa.document_verification_status, c.college_name, " +
            "co.course_name, sp.amount_due, sp.payment_status, sp.payment_date " +
            "FROM student_allotment sa " +
            "JOIN colleges c ON sa.college_id = c.college_id " +
            "JOIN courses co ON sa.course_id = co.course_id " +
            "LEFT JOIN student_payments sp ON sa.allotment_id = sp.allotment_id " +
            "WHERE sa.student_id = ? AND sa.acceptance_status = 'ACCEPTED'";

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.studentId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String docStatus = rs.getString("document_verification_status");
                String collegeName = rs.getString("college_name");
                String courseName = rs.getString("course_name");
                double amountDue = rs.getDouble("amount_due");
                String paymentStatus = rs.getString("payment_status");
                java.sql.Date paymentDate = rs.getDate("payment_date");

                JPanel infoPanel = new JPanel(new GridLayout(2, 2, 15, 15));
                infoPanel.setBackground(CARD_COLOR);
                infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                addInfoRow(infoPanel, "College:", collegeName, SECONDARY_COLOR);
                addInfoRow(infoPanel, "Course:", courseName, SECONDARY_COLOR);

                contentPanel.add(infoPanel);
                contentPanel.add(Box.createVerticalStrut(20));

                JPanel docStatusPanel = new JPanel(new BorderLayout());
                docStatusPanel.setBackground(docStatus != null && docStatus.contains("VERIFIED") ?
                    new Color(200, 230, 200) : new Color(255, 240, 200));
                docStatusPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                String docStatusText = docStatus != null ? docStatus : "PENDING";
                JLabel docLabel = new JLabel("📋 Document Verification Status: " + docStatusText);
                docLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                docLabel.setForeground(docStatus != null && docStatus.contains("VERIFIED") ?
                    new Color(0, 100, 0) : new Color(150, 100, 0));

                docStatusPanel.add(docLabel);
                contentPanel.add(docStatusPanel);
                contentPanel.add(Box.createVerticalStrut(20));

                if (docStatus != null && docStatus.contains("VERIFIED")) {
                    JPanel paymentPanel = new JPanel();
                    paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
                    paymentPanel.setBackground(CARD_COLOR);
                    paymentPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    ));

                    JLabel paymentHeaderLabel = new JLabel("💰 PAYMENT DETAILS - READY TO PAY!");
                    paymentHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    paymentHeaderLabel.setForeground(new Color(46, 204, 113));
                    paymentPanel.add(paymentHeaderLabel);
                    paymentPanel.add(Box.createVerticalStrut(15));

                    JLabel amountLabel = new JLabel("Amount Due: ₹ " + String.format("%.2f", amountDue));
                    amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    amountLabel.setForeground(TEXT_PRIMARY);
                    paymentPanel.add(amountLabel);
                    paymentPanel.add(Box.createVerticalStrut(15));

                    String statusDisplay = paymentStatus == null || "PENDING".equals(paymentStatus) ?
                        "⏳ AWAITING PAYMENT" : "PAID".equals(paymentStatus) ?
                        "✅ PAYMENT RECEIVED" : "❌ " + paymentStatus;

                    JLabel statusValueLabel = new JLabel(statusDisplay);
                    statusValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    statusValueLabel.setForeground("PAID".equals(paymentStatus) ?
                        new Color(0, 150, 0) : new Color(200, 150, 0));
                    paymentPanel.add(statusValueLabel);

                    contentPanel.add(paymentPanel);
                } else {
                    JPanel waitingPanel = new JPanel(new BorderLayout());
                    waitingPanel.setBackground(new Color(255, 245, 230));
                    waitingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                    JLabel waitingLabel = new JLabel("⏳ AWAITING DOCUMENT VERIFICATION");
                    waitingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    waitingLabel.setForeground(new Color(200, 150, 0));

                    JLabel descLabel = new JLabel(
                        "The college is verifying your documents. Payment details will appear once complete. Usually takes 1-2 business days."
                    );
                    descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    descLabel.setForeground(TEXT_PRIMARY);

                    waitingPanel.add(waitingLabel, BorderLayout.NORTH);
                    waitingPanel.add(descLabel, BorderLayout.SOUTH);

                    contentPanel.add(waitingPanel);
                }

            } else {
                JPanel noDataPanel = new JPanel();
                noDataPanel.setLayout(new BoxLayout(noDataPanel, BoxLayout.Y_AXIS));
                noDataPanel.setBackground(CARD_COLOR);

                JLabel noDataLabel = new JLabel("❌ No accepted allotment found");
                noDataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                noDataLabel.setForeground(TEXT_PRIMARY);
                noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel descLabel = new JLabel("Please accept your allotment first");
                descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                descLabel.setForeground(TEXT_SECONDARY);
                descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                noDataPanel.add(Box.createVerticalGlue());
                noDataPanel.add(noDataLabel);
                noDataPanel.add(Box.createVerticalStrut(5));
                noDataPanel.add(descLabel);
                noDataPanel.add(Box.createVerticalGlue());

                contentPanel.add(noDataPanel);
            }

            rs.close();

        } catch (java.sql.SQLException e) {
            System.out.println("❌ Error loading payment status: " + e.getMessage());
            e.printStackTrace();

            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
            errorPanel.setBackground(CARD_COLOR);

            JLabel errorLabel = new JLabel("❌ Error Loading Payment Status");
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            errorLabel.setForeground(ACCENT_COLOR);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            contentPanel.add(errorPanel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(BACKGROUND_COLOR);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addInfoRow(JPanel panel, String label, String value, Color labelColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComp.setForeground(labelColor);

        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueComp.setForeground(TEXT_PRIMARY);

        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.CENTER);

        panel.add(row);
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
    }

    private void updateStudentAcceptance(int studentId, String status) {
        String sql = "UPDATE student_allotment SET acceptance_status = ? " +
            "WHERE student_id = ? AND acceptance_status = 'PENDING'";

        try (java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, studentId);

            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this,
                    "✅ Your allotment status has been updated to: " + status,
                    "Success", JOptionPane.INFORMATION_MESSAGE);

                loadContent("Allotment Status");
            } else {
                JOptionPane.showMessageDialog(this,
                    "❌ Could not update status. Status may have changed or allotment not found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (java.sql.SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateAllotmentLetter() {
        try {
            String sql = "SELECT sa.student_id, s.full_name, s.neet_roll_number, s.neet_rank, " +
                "s.category, s.email, s.mobile_number, " +
                "col.college_name, col.state, col.city, " +
                "c.course_name, c.course_type, c.fees_per_year, " +
                "TO_CHAR(sa.allotment_date, 'DD-MON-YYYY') as allot_date " +
                "FROM student_allotment sa " +
                "JOIN students s ON sa.student_id = s.student_id " +
                "JOIN colleges col ON sa.college_id = col.college_id " +
                "JOIN courses c ON sa.course_id = c.course_id " +
                "WHERE sa.student_id = " + studentId;

            java.sql.Connection conn = com.neet.db.DatabaseConnection.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                    "No allotment found for your account!",
                    "Not Allotted",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            StringBuilder letter = new StringBuilder();
            letter.append("═══════════════════════════════════════════════════════════════\n");
            letter.append(" NEET UG COUNSELLING 2025\n");
            letter.append(" National Testing Agency (NTA)\n");
            letter.append("═══════════════════════════════════════════════════════════════\n\n");
            letter.append(" SEAT ALLOTMENT LETTER\n\n");
            letter.append("═══════════════════════════════════════════════════════════════\n\n");

            letter.append("CANDIDATE DETAILS:\n");
            letter.append("─────────────────────────────────────────────────────────────\n");
            letter.append(String.format("%-25s : %s\n", "Student Name", rs.getString("full_name")));
            letter.append(String.format("%-25s : %d\n", "Student ID", rs.getInt("student_id")));
            letter.append(String.format("%-25s : %s\n", "NEET Roll Number", rs.getString("neet_roll_number")));
            letter.append(String.format("%-25s : %d\n", "NEET Rank (AIR)", rs.getInt("neet_rank")));
            letter.append(String.format("%-25s : %s\n", "Category", rs.getString("category")));
            letter.append(String.format("%-25s : %s\n", "Email", rs.getString("email")));
            letter.append(String.format("%-25s : %s\n", "Mobile Number", rs.getString("mobile_number")));

            letter.append("\nALLOTMENT DETAILS:\n");
            letter.append("─────────────────────────────────────────────────────────────\n");
            letter.append("*** CONGRATULATIONS! ***\n");
            letter.append("You have been allotted a seat as per your choices and merit.\n\n");
            letter.append(String.format("%-25s : %s\n", "★ Allotted College", rs.getString("college_name")));
            letter.append(String.format("%-25s : %s\n", "★ Allotted Course", rs.getString("course_name")));
            letter.append(String.format("%-25s : %s\n", "Course Type", rs.getString("course_type")));
            letter.append(String.format("%-25s : %s\n", "College State", rs.getString("state")));
            letter.append(String.format("%-25s : %s\n", "College City", rs.getString("city")));
            letter.append(String.format("%-25s : %s\n", "Allotment Date", rs.getString("allot_date")));
            letter.append(String.format("%-25s : ₹ %.0f\n", "Annual Fees", rs.getDouble("fees_per_year")));

            letter.append("\nIMPORTANT INSTRUCTIONS:\n");
            letter.append("─────────────────────────────────────────────────────────────\n");
            letter.append("1. This allotment is provisional and subject to verification of documents.\n");
            letter.append("2. You must report to the allotted college within 7 days from the date of allotment.\n");
            letter.append("3. Carry this allotment letter along with all original documents for verification.\n");
            letter.append("4. Pay the requisite fees as per the college fee structure.\n");
            letter.append("═══════════════════════════════════════════════════════════════\n");

            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "AllotmentLetter_" + studentId + "_" + timestamp + ".txt";
            String userHome = System.getProperty("user.home");
            String filePath = userHome + java.io.File.separator + "Downloads" + java.io.File.separator + filename;

            java.io.FileWriter writer = new java.io.FileWriter(filePath);
            writer.write(letter.toString());
            writer.close();

            rs.close();
            stmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this,
                "✓ Allotment Letter generated successfully!\n\nFile saved at:\n" + filePath,
                "Download Successful",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error generating letter:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
