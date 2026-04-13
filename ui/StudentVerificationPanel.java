
package com.neet.ui;

import com.neet.dao.*;
import com.neet.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class StudentVerificationPanel extends JFrame {
    
    private int adminId;
    private int studentId;
    private JFrame parentFrame;
    private Student student;
    private JTable docTable;
    private DefaultTableModel docModel;

    public StudentVerificationPanel(int adminId, int studentId, JFrame parentFrame) {
        this.adminId = adminId;
        this.studentId = studentId;
        this.parentFrame = parentFrame;
        
        setTitle("Student Verification - " + studentId);
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        
        StudentDAO studentDAO = new StudentDAO();
        student = studentDAO.getStudentById(studentId);
        
        if (student != null) {
            initComponents();
            loadDocuments();
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // ==================== INFO PANEL ====================
        JPanel infoPanel = createInfoPanel();

        // ==================== DOCUMENT PANEL ====================
        JPanel docPanel = createDocumentPanel();

        // ==================== ACTION PANEL ====================
        JPanel actionPanel = createActionPanel();

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(docPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * ✅ Create Student Info Panel
     */
    /**
     * ✅ FIXED: Create Student Info Panel - No Overlapping
     */
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("📋 Student Information"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        infoPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);      // ✅ Increased padding
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==================== ROW 1 ====================
        // Column 0: Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.15;  // ✅ Reduced label width
        JLabel label1 = new JLabel("Student ID:");
        label1.setFont(new Font("Arial", Font.BOLD, 12));
        label1.setForeground(new Color(33, 33, 33));
        infoPanel.add(label1, gbc);

        // Column 1: Value
        gbc.gridx = 1;
        gbc.weightx = 0.35;  // ✅ More space for value
        JLabel value1 = new JLabel(String.valueOf(student.getStudentId()));
        value1.setFont(new Font("Arial", Font.PLAIN, 12));
        value1.setForeground(new Color(66, 66, 66));
        infoPanel.add(value1, gbc);

        // Column 2: Label
        gbc.gridx = 2;
        gbc.weightx = 0.15;  // ✅ Reduced label width
        JLabel label2 = new JLabel("Name:");
        label2.setFont(new Font("Arial", Font.BOLD, 12));
        label2.setForeground(new Color(33, 33, 33));
        infoPanel.add(label2, gbc);

        // Column 3: Value
        gbc.gridx = 3;
        gbc.weightx = 0.35;  // ✅ More space for value
        JLabel value2 = new JLabel(student.getFullName() != null ? student.getFullName() : "N/A");
        value2.setFont(new Font("Arial", Font.PLAIN, 12));
        value2.setForeground(new Color(66, 66, 66));
        infoPanel.add(value2, gbc);

        // ==================== ROW 2 ====================
        // Column 0: Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.15;
        JLabel label3 = new JLabel("Email:");
        label3.setFont(new Font("Arial", Font.BOLD, 12));
        label3.setForeground(new Color(33, 33, 33));
        infoPanel.add(label3, gbc);

        // Column 1: Value
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        JLabel value3 = new JLabel(student.getEmail() != null ? student.getEmail() : "N/A");
        value3.setFont(new Font("Arial", Font.PLAIN, 12));
        value3.setForeground(new Color(66, 66, 66));
        infoPanel.add(value3, gbc);

        // Column 2: Label
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        JLabel label4 = new JLabel("Phone:");
        label4.setFont(new Font("Arial", Font.BOLD, 12));
        label4.setForeground(new Color(33, 33, 33));
        infoPanel.add(label4, gbc);

        // Column 3: Value
        gbc.gridx = 3;
        gbc.weightx = 0.35;
        JLabel value4 = new JLabel(student.getMobileNumber() != null ? student.getMobileNumber() : "N/A");
        value4.setFont(new Font("Arial", Font.PLAIN, 12));
        value4.setForeground(new Color(66, 66, 66));
        infoPanel.add(value4, gbc);

        // ==================== ROW 3 ====================
        // Column 0: Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.15;
        JLabel label5 = new JLabel("NEET Rank:");
        label5.setFont(new Font("Arial", Font.BOLD, 12));
        label5.setForeground(new Color(33, 33, 33));
        infoPanel.add(label5, gbc);

        // Column 1: Value
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        JLabel value5 = new JLabel(String.valueOf(student.getNeetRank()));
        value5.setFont(new Font("Arial", Font.PLAIN, 12));
        value5.setForeground(new Color(66, 66, 66));
        infoPanel.add(value5, gbc);

        // Column 2: Label
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        JLabel label6 = new JLabel("Category:");
        label6.setFont(new Font("Arial", Font.BOLD, 12));
        label6.setForeground(new Color(33, 33, 33));
        infoPanel.add(label6, gbc);

        // Column 3: Value
        gbc.gridx = 3;
        gbc.weightx = 0.35;
        JLabel value6 = new JLabel(student.getCategory() != null ? student.getCategory() : "N/A");
        value6.setFont(new Font("Arial", Font.PLAIN, 12));
        value6.setForeground(new Color(66, 66, 66));
        infoPanel.add(value6, gbc);

        // ==================== ROW 4 ====================
        // Column 0: Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.15;
        JLabel label7 = new JLabel("Status:");
        label7.setFont(new Font("Arial", Font.BOLD, 12));
        label7.setForeground(new Color(33, 33, 33));
        infoPanel.add(label7, gbc);

        // Column 1: Value
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        JLabel value7 = new JLabel(student.getRegistrationStatus() != null ? student.getRegistrationStatus() : "N/A");
        value7.setFont(new Font("Arial", Font.PLAIN, 12));
        value7.setForeground(new Color(66, 66, 66));
        infoPanel.add(value7, gbc);

        // Column 2: Label
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        JLabel label8 = new JLabel("NEET Roll:");
        label8.setFont(new Font("Arial", Font.BOLD, 12));
        label8.setForeground(new Color(33, 33, 33));
        infoPanel.add(label8, gbc);

        // Column 3: Value
        gbc.gridx = 3;
        gbc.weightx = 0.35;
        JLabel value8 = new JLabel(student.getNeetRollNumber() != null ? student.getNeetRollNumber() : "N/A");
        value8.setFont(new Font("Arial", Font.PLAIN, 12));
        value8.setForeground(new Color(66, 66, 66));
        infoPanel.add(value8, gbc);

        return infoPanel;
    }


    /**
     * ✅ Create Document Panel
     */
    /**
     * ✅ Create Action Panel - Foreground changed to BLACK
     */
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(new Color(240, 240, 240));

        JButton approveButton = new JButton("✅ Approve");
        approveButton.setFont(new Font("Arial", Font.BOLD, 13));
        approveButton.setBackground(new Color(76, 175, 80));
        approveButton.setForeground(Color.BLACK);  // ✅ CHANGED TO BLACK
        approveButton.setFocusPainted(false);
        approveButton.addActionListener(e -> handleApprove());

        JButton rejectButton = new JButton("❌ Reject");
        rejectButton.setFont(new Font("Arial", Font.BOLD, 13));
        rejectButton.setBackground(new Color(244, 67, 54));
        rejectButton.setForeground(Color.BLACK);  // ✅ CHANGED TO BLACK
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> handleReject());

        JButton closeButton = new JButton("🔙 Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 13));
        closeButton.setBackground(new Color(150, 150, 150));
        closeButton.setForeground(Color.BLACK);  // ✅ CHANGED TO BLACK
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> this.dispose());

        actionPanel.add(approveButton);
        actionPanel.add(rejectButton);
        actionPanel.add(closeButton);

        return actionPanel;
    }

    /**
     * ✅ Create Document Panel - View button foreground changed to BLACK
     */
    private JPanel createDocumentPanel() {
        JPanel docPanel = new JPanel(new BorderLayout(10, 10));
        docPanel.setBorder(BorderFactory.createTitledBorder("📄 Uploaded Documents"));
        docPanel.setBackground(Color.WHITE);

        String[] columns = {"Document Type", "File Name", "Upload Date", "Action"};
        docModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 3;
            }
        };

        docTable = new JTable(docModel);
        docTable.setRowHeight(30);
        docTable.setFont(new Font("Arial", Font.PLAIN, 12));
        docTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        docTable.getColumnModel().getColumn(3).setWidth(100);

        docTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = docTable.rowAtPoint(evt.getPoint());
                int col = docTable.columnAtPoint(evt.getPoint());

                if (col == 3 && row >= 0) {
                    viewDocument(row);
                }
            }
        });

        // Render Preview button in Action column
        docTable.getColumnModel().getColumn(3).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("👁️ View");
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.setBackground(new Color(33, 150, 243));
            btn.setForeground(Color.BLACK);  // ✅ CHANGED TO BLACK
            btn.setFocusPainted(false);
            return btn;
        });

        JScrollPane scrollPane = new JScrollPane(docTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        docPanel.add(scrollPane, BorderLayout.CENTER);
        return docPanel;
    }

    /**
     * ✅ Add label-field pair to panel
     */
    private void addLabelField(JPanel panel, GridBagConstraints gbc, String label, String value) {
        // Label
        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Arial", Font.BOLD, 12));
        lblField.setForeground(new Color(33, 33, 33));
        panel.add(lblField, gbc);

        // Value
        gbc.gridx++;
        JLabel valueField = new JLabel(value != null ? value : "N/A");
        valueField.setFont(new Font("Arial", Font.PLAIN, 12));
        valueField.setForeground(new Color(66, 66, 66));
        panel.add(valueField, gbc);
    }

    /**
     * ✅ FIXED: Load documents from database with proper typing
     */
    private void loadDocuments() {
        docModel.setRowCount(0);
        
        StudentVerificationDAO verifyDAO = new StudentVerificationDAO();
        // ✅ FIX 1: Add generic type <String[]>
        List<String[]> docs = verifyDAO.getStudentDocuments(studentId);

        if (docs == null || docs.isEmpty()) {
            System.out.println("⚠️ No documents uploaded for student " + studentId);
            return;
        }
        
        System.out.println("✓ Loading " + docs.size() + " documents");
        
        // ✅ FIX 2: For loop now works correctly with generic type
        for (String[] doc : docs) {
            if (doc != null && doc.length >= 4) {
                // ✅ FIX 3: Add individual array elements, not the entire array
                docModel.addRow(new Object[]{
                    doc[0],           // Document Type
                    doc[1],           // File Name
                    doc[2],           // Upload Date
                    "👁️ View"        // Action button
                });
                System.out.println("  ✓ Added: " + doc[0] + " - " + doc[1]);
            }
        }
        
        System.out.println("✓ Total documents loaded: " + docModel.getRowCount());
    }

    /**
     * ✅ View selected document
     */
    /**
     * ✅ View selected document - FIXED: Added setVisible(true)
     */
    private void viewDocument(int row) {
        try {
            int selectedRow = row;
            
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, 
                        "Please select a document", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            System.out.println("✓ Viewing document at row: " + selectedRow);

            // Get data from table
            String docType = (String) docModel.getValueAt(selectedRow, 0);
            String fileName = (String) docModel.getValueAt(selectedRow, 1);

            System.out.println("✓ Document: " + docType + " - " + fileName);

            // Get file path from database
            StudentVerificationDAO verifyDAO = new StudentVerificationDAO();
            List<String[]> docs = verifyDAO.getStudentDocuments(studentId);

            String filePath = null;
            
            if (selectedRow < docs.size()) {
                String[] doc = docs.get(selectedRow);
                if (doc.length > 3) {
                    filePath = doc[3];  // Get file_path from index 3
                }
            }

            // Validate file path
            if (filePath == null || filePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "❌ File path not found in database!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("❌ File path is null for: " + fileName);
                return;
            }

            System.out.println("✓ File path: " + filePath);

            // Verify file exists
            File file = new File(filePath);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, 
                        "❌ File not found on disk!\n\nPath: " + filePath, 
                        "File Not Found", JOptionPane.ERROR_MESSAGE);
                System.out.println("❌ File does not exist: " + filePath);
                return;
            }

            System.out.println("✓ Opening document: " + filePath);
            System.out.println("✓ File size: " + (file.length() / 1024) + " KB");
            
            // ✅ FIX: Open document viewer dialog WITH setVisible(true)
            DocumentViewerDialog viewer = new DocumentViewerDialog(this, filePath);
            viewer.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "❌ Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * ✅ Approve student
     */
    private void handleApprove() {
        String comments = JOptionPane.showInputDialog(this, "Enter approval comments:", "");
        if (comments != null && !comments.trim().isEmpty()) {
            StudentVerificationDAO verifyDAO = new StudentVerificationDAO();
            if (verifyDAO.approveStudent(studentId, adminId, comments)) {
                JOptionPane.showMessageDialog(this, 
                        "✅ Student approved successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "❌ Error approving student", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * ✅ Reject student
     */
    private void handleReject() {
        String reason = JOptionPane.showInputDialog(this, "Enter rejection reason:", "");
        if (reason != null && !reason.trim().isEmpty()) {
            StudentVerificationDAO verifyDAO = new StudentVerificationDAO();
            if (verifyDAO.rejectStudent(studentId, adminId, reason)) {
                JOptionPane.showMessageDialog(this, 
                        "✅ Student rejected", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                        "❌ Error rejecting student", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}