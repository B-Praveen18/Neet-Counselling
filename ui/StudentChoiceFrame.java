package com.neet.ui;

import com.neet.dao.*;
import com.neet.model.Student;
import com.neet.model.College;
import com.neet.model.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Enhanced Student Choice Frame - Modern UI
 * All functionality preserved, only UI enhanced
 */
public class StudentChoiceFrame extends JFrame {
    
    // Modern Color Palette
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);      // Blue
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);    // Green
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);        // Red
    private static final Color WARNING_COLOR = new Color(241, 196, 15);      // Yellow
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Light Gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    private int studentId;
    private JTextField searchField;
    private JTable collegeTable;
    private JTable courseTable;
    private JTable selectedTable;
    private DefaultTableModel selectedModel;
    private int preferenceOrder = 1;
    
    public StudentChoiceFrame(int studentId) {
        this.studentId = studentId;
        setTitle("🏫 College Search & Choice Filling");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Check if choices are locked
        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.getStudentById(studentId);
        
        if (student != null && student.getChoiceLocked() != null && student.getChoiceLocked().equals("YES")) {
            JPanel lockPanel = createLockedPanel();
            add(lockPanel);
            return;
        }

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        
        // Results Panel
        JPanel resultsPanel = createResultsPanel();
        
        // Courses Panel
        JPanel coursesPanel = createCoursesPanel();
        
        // Choices Panel
        JPanel choicesPanel = createChoicesPanel();
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel(student);
        
        // Add top section (header + search)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        // Add middle section (college results + courses)
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        middlePanel.setOpaque(false);
        middlePanel.add(resultsPanel);
        middlePanel.add(coursesPanel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(choicesPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        
        add(mainPanel);
    }

    /**
     * Create locked panel when choices are locked
     */
    private JPanel createLockedPanel() {
        JPanel lockPanel = new JPanel(new BorderLayout(10, 10));
        lockPanel.setBackground(BACKGROUND_COLOR);
        lockPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lockedLabel = new JLabel("❌ Your choices have been LOCKED!");
        lockedLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lockedLabel.setForeground(ACCENT_COLOR);
        lockedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("You cannot add or modify choices after locking.");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(lockedLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(messageLabel);
        cardPanel.add(Box.createVerticalGlue());
        
        JButton closeButton = createStyledButton("⬅ Close", new Color(108, 117, 125), Color.BLACK);
        closeButton.addActionListener(e -> this.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        
        lockPanel.add(cardPanel, BorderLayout.CENTER);
        lockPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return lockPanel;
    }

    /**
     * Create modern header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🏫 College Search & Choice Filling");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel infoLabel = new JLabel("Search colleges, explore courses, and fill your preferences");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(236, 240, 241));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(3));
        leftPanel.add(infoLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        return headerPanel;
    }

    /**
     * Create search panel with modern styling
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel searchLabel = new JLabel("🔍 Search by Name/Code:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(TEXT_PRIMARY);

        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(250, 35));

        JButton searchButton = createStyledButton("🔍 Search", PRIMARY_COLOR, Color.BLACK);
        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    /**
     * Create college results panel
     */
    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout(10, 10));
        resultsPanel.setBackground(CARD_COLOR);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("🏢 College Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] collegeColumns = {"College ID", "College Name", "City", "Type"};
        DefaultTableModel collegeModel = new DefaultTableModel(collegeColumns, 0);
        collegeTable = new JTable(collegeModel);
        styleModernTable(collegeTable);

        collegeTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = collegeTable.getSelectedRow();
            if (selectedRow >= 0) {
                int collegeId = (Integer) collegeTable.getValueAt(selectedRow, 0);
                loadCoursesForCollege(collegeId);
            }
        });

        JScrollPane collegeScroll = new JScrollPane(collegeTable);
        collegeScroll.setBorder(null);

        resultsPanel.add(titleLabel, BorderLayout.NORTH);
        resultsPanel.add(collegeScroll, BorderLayout.CENTER);

        return resultsPanel;
    }

    /**
     * Create courses panel
     */
    private JPanel createCoursesPanel() {
        JPanel coursesPanel = new JPanel(new BorderLayout(10, 10));
        coursesPanel.setBackground(CARD_COLOR);
        coursesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("📚 Available Courses");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] courseColumns = {"Course ID", "Course Name", "Type", "Seats", "Fees/Year"};
        DefaultTableModel courseModel = new DefaultTableModel(courseColumns, 0);
        courseTable = new JTable(courseModel);
        styleModernTable(courseTable);

        JScrollPane courseScroll = new JScrollPane(courseTable);
        courseScroll.setBorder(null);

        coursesPanel.add(titleLabel, BorderLayout.NORTH);
        coursesPanel.add(courseScroll, BorderLayout.CENTER);

        return coursesPanel;
    }

    /**
     * Create selected choices panel
     */
    private JPanel createChoicesPanel() {
        JPanel choicesPanel = new JPanel(new BorderLayout(10, 10));
        choicesPanel.setBackground(CARD_COLOR);
        choicesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("✓ Your Selected Choices");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_PRIMARY);

        String[] choiceColumns = {"Preference", "College", "Course", "Type", "Fees/Year"};
        selectedModel = new DefaultTableModel(choiceColumns, 0);
        selectedTable = new JTable(selectedModel);
        styleModernTable(selectedTable);
        selectedTable.setPreferredScrollableViewportSize(new Dimension(0, 120));

        JScrollPane choicesScroll = new JScrollPane(selectedTable);
        choicesScroll.setBorder(null);

        choicesPanel.add(titleLabel, BorderLayout.NORTH);
        choicesPanel.add(choicesScroll, BorderLayout.CENTER);

        return choicesPanel;
    }

    /**
     * Create button panel
     */
    private JPanel createButtonPanel(Student student) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addButton = createStyledButton("➕ Add to Choices", SECONDARY_COLOR, Color.BLACK);
        addButton.addActionListener(e -> addChoice());

        JButton lockButton = createStyledButton("🔒 Lock Choices", PRIMARY_COLOR, Color.BLACK);
        lockButton.addActionListener(e -> lockChoices());

        JButton backButton = createStyledButton("⬅ Back", new Color(108, 117, 125), Color.BLACK);
        backButton.addActionListener(e -> this.dispose());

        // Disable if locked
        if (student != null && student.getChoiceLocked() != null && student.getChoiceLocked().equals("YES")) {
            addButton.setEnabled(false);
            lockButton.setEnabled(false);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    /**
     * Style table with modern design
     */
    private void styleModernTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(30);
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(new Color(52, 152, 219, 80));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(189, 195, 199));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(189, 195, 199));
                c.setForeground(Color.WHITE);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    /**
     * Create styled button
     */
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
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

    /**
     * Perform search - Functionality preserved
     */
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter search term", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        CollegeDAO collegeDAO = new CollegeDAO();
        List<College> colleges = collegeDAO.searchCollegesByNameOrCode(searchTerm);
        
        DefaultTableModel collegeModel = (DefaultTableModel) collegeTable.getModel();
        collegeModel.setRowCount(0);
        for (College college : colleges) {
            collegeModel.addRow(new Object[]{
                college.getCollegeId(),
                college.getCollegeName(),
                college.getCity(),
                college.getCollegeType()
            });
        }
    }

    /**
     * Add choice - Functionality preserved
     */
    private void addChoice() {
        int collegeRow = collegeTable.getSelectedRow();
        int courseRow = courseTable.getSelectedRow();
        
        if (collegeRow < 0 || courseRow < 0) {
            JOptionPane.showMessageDialog(this, "Select both a college and a course!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String prefStr = JOptionPane.showInputDialog(this, "Enter preference number for this choice (1-20):");
        if (prefStr == null || prefStr.trim().isEmpty()) return;
        
        int preferenceOrder;
        try {
            preferenceOrder = Integer.parseInt(prefStr.trim());
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid preference! Enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (preferenceOrder < 1 || preferenceOrder > 20) {
            JOptionPane.showMessageDialog(this, "❌ Preference must be between 1 and 20!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StudentChoiceDAO choiceDAO = new StudentChoiceDAO();
        if (choiceDAO.preferenceExists(studentId, preferenceOrder)) {
            JOptionPane.showMessageDialog(this, 
                "❌ Preference " + preferenceOrder + " already exists!\n\nPlease use a different preference number (1-20).", 
                "Duplicate Preference", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int totalChoices = choiceDAO.getChoiceCount(studentId);
        if (totalChoices >= 20) {
            JOptionPane.showMessageDialog(this, 
                "❌ Maximum 20 choices allowed!\n\nYou already have 20 choices.", 
                "Max Choices Reached", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int collegeId = (Integer) collegeTable.getValueAt(collegeRow, 0);
        int courseId = (Integer) courseTable.getValueAt(courseRow, 0);

        boolean success = choiceDAO.addChoice(studentId, collegeId, courseId, preferenceOrder);

        if (success) {
            JOptionPane.showMessageDialog(this, 
                "✅ Choice added to preferences!\n\nPreference: " + preferenceOrder + "\nTotal Choices: " + (totalChoices + 1) + "/20",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "❌ Failed to add choice!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lock choices - Functionality preserved
     */
    private void lockChoices() {
        StudentDAO studentDAO = new StudentDAO();
        if (studentDAO.lockChoices(studentId)) {
            JOptionPane.showMessageDialog(this, 
                "✅ Choices locked successfully!\n\nYou cannot modify choices now.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "❌ Failed to lock choices!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load courses - Functionality preserved
     */
    private void loadCoursesForCollege(int collegeId) {
        CollegeDAO collegeDAO = new CollegeDAO();
        List<Course> courses = collegeDAO.getCoursesByCollegeId(collegeId);

        DefaultTableModel courseModel = (DefaultTableModel) courseTable.getModel();
        courseModel.setRowCount(0);

        for (Course course : courses) {
            courseModel.addRow(new Object[]{
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseType(),
                course.getAvailableSeats(),
                String.format("₹ %.2f", course.getFeesPerYear())
            });
        }
    }
}
