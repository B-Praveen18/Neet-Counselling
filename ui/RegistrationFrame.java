package com.neet.ui;

import com.neet.dao.StudentDAO;
import com.neet.model.Student;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Enhanced NEET Counselling Registration Form - Modern UI
 * All functionality preserved, only UI enhanced
 */
public class RegistrationFrame extends JFrame {
    
    // Modern Color Palette
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);      // Blue
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);    // Green
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);        // Red
    private static final Color WARNING_COLOR = new Color(241, 196, 15);      // Yellow
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Light Gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    // Login credentials
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    
    // Personal & Contact Information
    private JTextField fullNameField, emailField, mobileField;
    private JTextField fatherNameField, motherNameField;
    private JSpinner dobSpinner;
    private JComboBox<String> genderCombo;
    private JTextArea permanentAddressArea;
    
    // NEET Score Details
    private JTextField neetRollField, neetApplicationField;
    private JSpinner neetScoreSpinner, neetRankSpinner, percentileSpinner;
    
    // Academic Details
    private JTextField class10SchoolField, class10BoardField, class10YearField;
    private JTextField class12SchoolField, class12BoardField, class12YearField;
    private JTextField class10MarksField, class12MarksField;
    private JTextField pcbMarksField;
    
    // Identity & Category
    private JTextField aadharField;
    private JComboBox<String> categoryCombo, identityProofCombo;
    private JTextField identityNumberField;
    private JComboBox<String> stateCombo;
    
    // Document Upload
    private Map<String, String> uploadedFiles = new HashMap<>();
    private Map<String, JLabel> statusLabels = new HashMap<>();
    private Map<String, JButton> uploadButtons = new HashMap<>();
    
    private JButton registerButton, backButton;
    
    // Validation Patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern NEET_ROLL_PATTERN = Pattern.compile("^\\d{10,15}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern AADHAR_PATTERN = Pattern.compile("^\\d{12}$");
    private static final Pattern YEAR_PATTERN = Pattern.compile("^(19|20)\\d{2}$");

    public RegistrationFrame() {
        setTitle("🏥 NEET Counselling 2025 - Student Registration");
        setSize(1100, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title Panel - Modern Header
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(PRIMARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("🏥 NEET COUNSELLING 2025 - STUDENT REGISTRATION");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // ==================== LOGIN CREDENTIALS ====================
        addSectionHeader(formPanel, gbc, row++, "🔐 LOGIN CREDENTIALS");
        addFormField(formPanel, gbc, row++, "Username*:", usernameField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Password*:", passwordField = new JPasswordField(25));
        addFormField(formPanel, gbc, row++, "Confirm Password*:", confirmPasswordField = new JPasswordField(25));

        // ==================== PERSONAL & CONTACT INFORMATION ====================
        addSectionHeader(formPanel, gbc, row++, "👤 PERSONAL & CONTACT INFORMATION");
        addFormField(formPanel, gbc, row++, "Full Name (as per NEET)*:", fullNameField = new JTextField(25));
        
        SpinnerDateModel dobModel = new SpinnerDateModel();
        dobSpinner = new JSpinner(dobModel);
        JSpinner.DateEditor dobEditor = new JSpinner.DateEditor(dobSpinner, "dd/MM/yyyy");
        dobSpinner.setEditor(dobEditor);
        addFormField(formPanel, gbc, row++, "Date of Birth*:", dobSpinner);
        
        String[] genders = {"MALE", "FEMALE", "TRANSGENDER"};
        genderCombo = new JComboBox<>(genders);
        addFormField(formPanel, gbc, row++, "Gender*:", genderCombo);
        
        addFormField(formPanel, gbc, row++, "Father's Name*:", fatherNameField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Mother's Name*:", motherNameField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Email ID*:", emailField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Mobile Number*:", mobileField = new JTextField(25));
        
        permanentAddressArea = new JTextArea(3, 25);
        permanentAddressArea.setLineWrap(true);
        permanentAddressArea.setWrapStyleWord(true);
        permanentAddressArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        permanentAddressArea.setForeground(TEXT_PRIMARY);
        JScrollPane addressScroll = new JScrollPane(permanentAddressArea);
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(createBoldLabel("Permanent Address*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressScroll, gbc);
        row++;

        // ==================== NEET SCORE DETAILS ====================
        addSectionHeader(formPanel, gbc, row++, "📊 NEET 2025 SCORE DETAILS");
        addFormField(formPanel, gbc, row++, "NEET Roll Number*:", neetRollField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "NEET Application Number*:", neetApplicationField = new JTextField(25));
        
        neetRankSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 2500000, 1));
        addFormField(formPanel, gbc, row++, "All India Rank (AIR)*:", neetRankSpinner);
        
        neetScoreSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 720, 1));
        addFormField(formPanel, gbc, row++, "NEET Score (out of 720)*:", neetScoreSpinner);
        
        percentileSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 0.01));
        addFormField(formPanel, gbc, row++, "NEET Percentile*:", percentileSpinner);

        // ==================== ACADEMIC DETAILS ====================
        addSectionHeader(formPanel, gbc, row++, "🎓 ACADEMIC DETAILS");
        
        addSubSectionHeader(formPanel, gbc, row++, "Class 10 Details");
        addFormField(formPanel, gbc, row++, "School Name*:", class10SchoolField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Board*:", class10BoardField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Passing Year*:", class10YearField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Percentage/CGPA*:", class10MarksField = new JTextField(25));
        
        addSubSectionHeader(formPanel, gbc, row++, "Class 12 Details (PCB)");
        addFormField(formPanel, gbc, row++, "School Name*:", class12SchoolField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Board*:", class12BoardField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Passing Year*:", class12YearField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "Percentage/CGPA*:", class12MarksField = new JTextField(25));
        addFormField(formPanel, gbc, row++, "PCB Total Marks*:", pcbMarksField = new JTextField(25));

        // ==================== IDENTITY PROOF ====================
        addSectionHeader(formPanel, gbc, row++, "🆔 IDENTITY PROOF");
        addFormField(formPanel, gbc, row++, "Aadhaar Number*:", aadharField = new JTextField(25));
        
        String[] identityTypes = {"Aadhaar Card", "Passport", "PAN Card", "Voter ID"};
        identityProofCombo = new JComboBox<>(identityTypes);
        addFormField(formPanel, gbc, row++, "Identity Proof Type*:", identityProofCombo);
        addFormField(formPanel, gbc, row++, "Identity Number*:", identityNumberField = new JTextField(25));

        // ==================== CATEGORY & DOMICILE ====================
        addSectionHeader(formPanel, gbc, row++, "📋 CATEGORY & DOMICILE");
        
        String[] categories = {"GENERAL", "SC", "ST", "OBC-NCL", "EWS", "PwD"};
        categoryCombo = new JComboBox<>(categories);
        addFormField(formPanel, gbc, row++, "Category*:", categoryCombo);
        
        String[] states = {"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
            "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
            "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
        stateCombo = new JComboBox<>(states);
        addFormField(formPanel, gbc, row++, "State (Domicile)*:", stateCombo);

        // ==================== DOCUMENT UPLOADS ====================
        addSectionHeader(formPanel, gbc, row++, "📄 DOCUMENT UPLOADS (All documents required)");
        
        row = addDocumentUploadField(formPanel, gbc, row, "1. NEET Scorecard / Rank Letter*", "NEET_SCORECARD");
        row = addDocumentUploadField(formPanel, gbc, row, "2. Class 10 Mark Sheet / Certificate*", "CLASS_10_MARKSHEET");
        row = addDocumentUploadField(formPanel, gbc, row, "3. Class 12 Mark Sheet / Certificate*", "CLASS_12_MARKSHEET");
        row = addDocumentUploadField(formPanel, gbc, row, "4. Aadhaar Card / Identity Proof*", "IDENTITY_PROOF");
        row = addDocumentUploadField(formPanel, gbc, row, "5. Category Certificate (SC/ST/OBC/EWS)", "CATEGORY_CERTIFICATE");
        row = addDocumentUploadField(formPanel, gbc, row, "6. PwD Certificate (if applicable)", "PWD_CERTIFICATE");
        row = addDocumentUploadField(formPanel, gbc, row, "7. Domicile Certificate (for state quota)*", "DOMICILE_CERTIFICATE");

        // Important Information Box
        JPanel infoPanel = createInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        formPanel.add(infoPanel, gbc);
        gbc.gridwidth = 1;

        // ==================== BUTTONS ====================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        registerButton = createStyledButton("✓ REGISTER", SECONDARY_COLOR, Color.WHITE);
        registerButton.setPreferredSize(new Dimension(160, 45));
        registerButton.addActionListener(e -> handleRegistration());

        backButton = createStyledButton("← BACK", new Color(108, 117, 125), Color.WHITE);
        backButton.setPreferredSize(new Dimension(160, 45));
        backButton.addActionListener(e -> {
            this.dispose();
            new HomePage().setVisible(true);
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void addSectionHeader(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(10, 0, 8, 0)
        ));
        
        panel.add(headerLabel, gbc);
        gbc.gridwidth = 1;
    }

    private void addSubSectionHeader(JPanel panel, GridBagConstraints gbc, int row, String title) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        
        JLabel headerLabel = new JLabel("   " + title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        headerLabel.setForeground(TEXT_SECONDARY);
        
        panel.add(headerLabel, gbc);
        gbc.gridwidth = 1;
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private int addDocumentUploadField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String docType) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel(labelText), gbc);

        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        uploadPanel.setBackground(CARD_COLOR);

        JButton uploadButton = createStyledButton("📁 Choose File", SECONDARY_COLOR, Color.WHITE);
        uploadButton.setPreferredSize(new Dimension(140, 32));
        uploadButton.addActionListener(e -> handleFileUpload(docType));

        JLabel statusLabel = new JLabel("No file chosen");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        statusLabel.setForeground(TEXT_SECONDARY);

        uploadPanel.add(uploadButton);
        uploadPanel.add(statusLabel);

        uploadButtons.put(docType, uploadButton);
        statusLabels.put(docType, statusLabel);

        gbc.gridx = 1;
        panel.add(uploadPanel, gbc);

        return row + 1;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(new Color(255, 248, 225));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WARNING_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JTextArea infoText = new JTextArea(
            "📌 IMPORTANT INSTRUCTIONS:\n\n" +
            "✓ All fields marked with * are mandatory\n" +
            "✓ Use the same Email ID and Mobile Number as registered in NEET application\n" +
            "✓ Supported document formats: PDF, JPG, JPEG, PNG\n" +
            "✓ Maximum file size per document: 2 MB\n" +
            "✓ Category certificate required ONLY for SC/ST/OBC/EWS/PwD candidates\n" +
            "✓ Domicile certificate required for state quota counselling\n" +
            "✓ Ensure all documents are clear and readable\n" +
            "✓ Documents will be verified by admin before counselling\n\n" +
            "⚠️ Incomplete or incorrect information may lead to rejection"
        );
        infoText.setEditable(false);
        infoText.setBackground(new Color(255, 248, 225));
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoText.setForeground(TEXT_PRIMARY);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);

        infoPanel.add(infoText, BorderLayout.CENTER);
        return infoPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
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

    private void handleFileUpload(String docType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select " + docType.replace("_", " "));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Documents ( *.jpg, *.jpeg, *.png)", 
             "jpg", "jpeg", "png"
        );
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            long fileSizeInMB = selectedFile.length() / (1024 * 1024);
            if (fileSizeInMB > 2) {
                JOptionPane.showMessageDialog(this,
                    "File size exceeds 2 MB!\nPlease choose a smaller file.",
                    "File Too Large",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            uploadedFiles.put(docType, selectedFile.getAbsolutePath());
            
            JLabel statusLabel = statusLabels.get(docType);
            JButton uploadButton = uploadButtons.get(docType);
            
            if (statusLabel != null) {
                statusLabel.setText("✓ " + selectedFile.getName());
                statusLabel.setForeground(SECONDARY_COLOR);
            }
            
            if (uploadButton != null) {
                uploadButton.setBackground(SECONDARY_COLOR);
                uploadButton.setText("✓ Uploaded");
            }
        }
    }

    private void handleRegistration() {
        System.out.println("=== Starting Registration Process ===");
        
        List<String> errors = validateAllFields();
        
        if (!errors.isEmpty()) {
            showAllErrors(errors);
            return;
        }

        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String mobile = mobileField.getText().trim();
            Date dob = (Date) dobSpinner.getValue();
            String gender = (String) genderCombo.getSelectedItem();
            String category = (String) categoryCombo.getSelectedItem();
            String neetRoll = neetRollField.getText().trim();
            int neetScore = (Integer) neetScoreSpinner.getValue();
            int neetRank = (Integer) neetRankSpinner.getValue();
            double percentile = (Double) percentileSpinner.getValue();
            String state = (String) stateCombo.getSelectedItem();
            String address = permanentAddressArea.getText().trim();
            String aadhar = aadharField.getText().trim();
            String fatherName = fatherNameField.getText().trim();
            String motherName = motherNameField.getText().trim();
            String neetApp = neetApplicationField.getText().trim();
            String class10School = class10SchoolField.getText().trim();
            String class10Board = class10BoardField.getText().trim();
            String class10Year = class10YearField.getText().trim();
            String class10Marks = class10MarksField.getText().trim();
            String class12School = class12SchoolField.getText().trim();
            String class12Board = class12BoardField.getText().trim();
            String class12Year = class12YearField.getText().trim();
            String class12Marks = class12MarksField.getText().trim();
            String pcbMarks = pcbMarksField.getText().trim();
            String identityType = (String) identityProofCombo.getSelectedItem();
            String identityNum = identityNumberField.getText().trim();
            
            Student student = new Student(
                username,           // 1
                password,           // 2
                fullName,           // 3
                email,              // 4
                mobile,             // 5
                dob,                // 6
                gender,             // 7
                category,           // 8
                neetRoll,           // 9
                neetScore,          // 10
                neetRank,           // 11
                percentile,         // 12
                state,              // 13
                "",                 // 14
                address,            // 15
                aadhar              // 16
            );
            
            student.setMobileNumber(mobile);
            student.setPermanentAddress(address);
            student.setFatherName(fatherName);
            student.setMotherName(motherName);
            student.setNeetApplicationNumber(neetApp);
            student.setClass10School(class10School);
            student.setClass10Board(class10Board);
            student.setClass10Year(class10Year);
            student.setClass10Marks(class10Marks);
            student.setClass12School(class12School);
            student.setClass12Board(class12Board);
            student.setClass12Year(class12Year);
            student.setClass12Marks(class12Marks);
            student.setPcbMarks(pcbMarks);
            student.setIdentityProofType(identityType);
            student.setIdentityNumber(identityNum);
            student.setStateDomicile(state);
            student.setDocuments(uploadedFiles);
            
            StudentDAO studentDAO = new StudentDAO();
            if (studentDAO.registerStudent(student)) {
                JOptionPane.showMessageDialog(this,
                    "✓ Registration Successful!\n\n" +
                    "Your NEET Counselling registration has been submitted.\n" +
                    "Documents uploaded successfully.\n\n" +
                    "Your application will be verified by admin within 2-3 working days.\n" +
                    "You will receive an email notification once verified.\n\n" +
                    "Thank you!",
                    "Registration Success",
                    JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new HomePage().setVisible(true);
            } else {
                showError("Registration failed!\n\nPlease check console for details.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Error during registration: " + ex.getMessage());
        }
    }

    private List<String> validateAllFields() {
        List<String> errors = new ArrayList<>();
        
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            errors.add("• Username is required");
        } else if (username.length() < 4) {
            errors.add("• Username must be at least 4 characters");
        }

        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (password.isEmpty()) {
            errors.add("• Password is required");
        } else {
            if (password.length() < 6) errors.add("• Password must be at least 6 characters");
            if (!password.matches(".*[A-Z].*")) errors.add("• Password must contain uppercase letter");
            if (!password.matches(".*[a-z].*")) errors.add("• Password must contain lowercase letter");
            if (!password.matches(".*\\d.*")) errors.add("• Password must contain a digit");
            if (!password.matches(".*[@#$%^&+=!].*")) errors.add("• Password must contain special character");
        }
        
        if (!password.equals(confirmPassword)) {
            errors.add("• Passwords do not match");
        }

        if (fullNameField.getText().trim().isEmpty()) errors.add("• Full Name is required");
        if (fatherNameField.getText().trim().isEmpty()) errors.add("• Father's Name is required");
        if (motherNameField.getText().trim().isEmpty()) errors.add("• Mother's Name is required");
        
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.add("• Email is required");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("• Invalid Email format");
        }
        
        String mobile = mobileField.getText().trim();
        if (mobile.isEmpty()) {
            errors.add("• Mobile number is required");
        } else if (!MOBILE_PATTERN.matcher(mobile).matches()) {
            errors.add("• Invalid Mobile number (10 digits, starts with 6-9)");
        }
        
        if (permanentAddressArea.getText().trim().isEmpty()) {
            errors.add("• Permanent Address is required");
        }

        String neetRoll = neetRollField.getText().trim();
        if (neetRoll.isEmpty()) {
            errors.add("• NEET Roll Number is required");
        } else if (!NEET_ROLL_PATTERN.matcher(neetRoll).matches()) {
            errors.add("• Invalid NEET Roll Number format");
        }
        
        if (neetApplicationField.getText().trim().isEmpty()) {
            errors.add("• NEET Application Number is required");
        }
        
        if ((Integer) neetScoreSpinner.getValue() <= 0) {
            errors.add("• NEET Score must be greater than 0");
        }

        if (class10SchoolField.getText().trim().isEmpty()) errors.add("• Class 10 School Name is required");
        if (class10BoardField.getText().trim().isEmpty()) errors.add("• Class 10 Board is required");
        
        String class10Year = class10YearField.getText().trim();
        if (class10Year.isEmpty()) {
            errors.add("• Class 10 Passing Year is required");
        } else if (!YEAR_PATTERN.matcher(class10Year).matches()) {
            errors.add("• Invalid Class 10 Year (format: YYYY)");
        }
        
        if (class10MarksField.getText().trim().isEmpty()) errors.add("• Class 10 Marks are required");
        
        if (class12SchoolField.getText().trim().isEmpty()) errors.add("• Class 12 School Name is required");
        if (class12BoardField.getText().trim().isEmpty()) errors.add("• Class 12 Board is required");
        
        String class12Year = class12YearField.getText().trim();
        if (class12Year.isEmpty()) {
            errors.add("• Class 12 Passing Year is required");
        } else if (!YEAR_PATTERN.matcher(class12Year).matches()) {
            errors.add("• Invalid Class 12 Year (format: YYYY)");
        }
        
        if (class12MarksField.getText().trim().isEmpty()) errors.add("• Class 12 Marks are required");
        if (pcbMarksField.getText().trim().isEmpty()) errors.add("• PCB Total Marks are required");

        String aadhar = aadharField.getText().trim();
        if (aadhar.isEmpty()) {
            errors.add("• Aadhaar Number is required");
        } else if (!AADHAR_PATTERN.matcher(aadhar).matches()) {
            errors.add("• Invalid Aadhaar Number (12 digits)");
        }
        
        if (identityNumberField.getText().trim().isEmpty()) {
            errors.add("• Identity Proof Number is required");
        }

        if (!uploadedFiles.containsKey("NEET_SCORECARD")) {
            errors.add("• NEET Scorecard is required");
        }
        if (!uploadedFiles.containsKey("CLASS_10_MARKSHEET")) {
            errors.add("• Class 10 Mark Sheet is required");
        }
        if (!uploadedFiles.containsKey("CLASS_12_MARKSHEET")) {
            errors.add("• Class 12 Mark Sheet is required");
        }
        if (!uploadedFiles.containsKey("IDENTITY_PROOF")) {
            errors.add("• Identity Proof is required");
        }
        if (!uploadedFiles.containsKey("DOMICILE_CERTIFICATE")) {
            errors.add("• Domicile Certificate is required");
        }
        
        String category = (String) categoryCombo.getSelectedItem();
        if (!category.equals("GENERAL") && !uploadedFiles.containsKey("CATEGORY_CERTIFICATE")) {
            errors.add("• Category Certificate is required for " + category);
        }
        
        if (category.equals("PwD") && !uploadedFiles.containsKey("PWD_CERTIFICATE")) {
            errors.add("• PwD Certificate is required");
        }

        return errors;
    }

    private void showAllErrors(List<String> errors) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("⚠️ Please fix the following errors:\n\n");
        
        for (String error : errors) {
            errorMessage.append(error).append("\n");
        }
        
        JTextArea textArea = new JTextArea(errorMessage.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(255, 245, 245));
        textArea.setForeground(ACCENT_COLOR);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "⚠️ Validation Errors (" + errors.size() + " issues found)",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationFrame().setVisible(true));
    }
}
