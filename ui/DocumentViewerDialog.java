package com.neet.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

/**
 * Enhanced Document Viewer Dialog
 * ✨ FIXED: Now supports both JFrame and JDialog parents
 * - Displays PDF documents
 * - Shows images (JPG, PNG, GIF, etc.)
 * - Document preview functionality
 */
public class DocumentViewerDialog extends JDialog {
    private String filePath;
    private JLabel fileLabel;
    private JScrollPane scrollPane;

    // ✨ FIXED: Constructor that accepts Dialog as parent (works with JDialog)
    public DocumentViewerDialog(Dialog parent, String filePath) {
        super(parent, "Document Viewer", true);
        this.filePath = filePath;
        initComponents();
    }

    // ✨ Constructor that accepts JFrame as parent
    public DocumentViewerDialog(JFrame parent, String filePath) {
        super(parent, "Document Viewer", true);
        this.filePath = filePath;
        initComponents();
    }

    // ✨ Default constructor for backward compatibility
    public DocumentViewerDialog(String filePath) {
        super((JFrame) null, "Document Viewer", true);
        this.filePath = filePath;
        initComponents();
    }

    private void initComponents() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with file info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel titleLabel = new JLabel("📄 Document Preview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topPanel.add(titleLabel, BorderLayout.WEST);

        fileLabel = new JLabel("File: " + (filePath != null ? new File(filePath).getName() : "Unknown"));
        fileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fileLabel.setForeground(new Color(100, 100, 100));
        topPanel.add(fileLabel, BorderLayout.EAST);

        // Center panel with document viewer
        JPanel centerPanel = new JPanel(new BorderLayout());

        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);

            if (!file.exists()) {
                JLabel errorLabel = new JLabel("<html><b>❌ Document Not Found</b><br/>Path: " + filePath + "</html>");
                errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                errorLabel.setForeground(Color.RED);
                errorLabel.setHorizontalAlignment(JLabel.CENTER);
                centerPanel.add(errorLabel, BorderLayout.CENTER);
            } else if (filePath.toLowerCase().endsWith(".pdf")) {
                // For PDF files - show placeholder with open option
                JPanel pdfPanel = new JPanel(new BorderLayout());
                JLabel pdfLabel = new JLabel("<html><b>📄 PDF Document</b><br/>File: " + file.getName() + 
                                           "<br/>Size: " + formatFileSize(file.length()) + "</html>");
                pdfLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                pdfLabel.setHorizontalAlignment(JLabel.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton openButton = new JButton("🔓 Open PDF");
                openButton.addActionListener(e -> openFile(filePath));
                buttonPanel.add(openButton);

                pdfPanel.add(pdfLabel, BorderLayout.CENTER);
                pdfPanel.add(buttonPanel, BorderLayout.SOUTH);
                centerPanel.add(pdfPanel, BorderLayout.CENTER);
            } else if (isImageFile(filePath)) {
                // For image files - display directly
                try {
                    ImageIcon icon = new ImageIcon(filePath);
                    
                    // Scale image if too large
                    if (icon.getIconWidth() > 700 || icon.getIconHeight() > 500) {
                        Image scaledImage = icon.getImage().getScaledInstance(
                            Math.min(icon.getIconWidth(), 700),
                            Math.min(icon.getIconHeight(), 500),
                            Image.SCALE_SMOOTH
                        );
                        icon = new ImageIcon(scaledImage);
                    }

                    JLabel imageLabel = new JLabel(icon);
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);
                    
                    scrollPane = new JScrollPane(imageLabel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    centerPanel.add(scrollPane, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel errorLabel = new JLabel("<html><b>❌ Error Loading Image</b><br/>" + e.getMessage() + "</html>");
                    errorLabel.setForeground(Color.RED);
                    centerPanel.add(errorLabel, BorderLayout.CENTER);
                }
            } else {
                // For text files or other formats
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    JTextArea textArea = new JTextArea(content);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Consolas", Font.PLAIN, 11));
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);

                    scrollPane = new JScrollPane(textArea);
                    centerPanel.add(scrollPane, BorderLayout.CENTER);
                } catch (Exception e) {
                    JLabel errorLabel = new JLabel("<html><b>❌ Error Reading File</b><br/>" + e.getMessage() + "</html>");
                    errorLabel.setForeground(Color.RED);
                    centerPanel.add(errorLabel, BorderLayout.CENTER);
                }
            }
        } else {
            JLabel noFileLabel = new JLabel("❌ No file path provided");
            noFileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            noFileLabel.setForeground(Color.RED);
            noFileLabel.setHorizontalAlignment(JLabel.CENTER);
            centerPanel.add(noFileLabel, BorderLayout.CENTER);
        }

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton openExternalButton = new JButton("🔓 Open External");
        openExternalButton.addActionListener(e -> {
            if (filePath != null && !filePath.isEmpty()) {
                openFile(filePath);
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(openExternalButton);
        bottomPanel.add(closeButton);

        // Add all panels
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // ✨ Open file with default application
    private void openFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(this, "Desktop operations not supported", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "File not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✨ Check if file is an image
    private boolean isImageFile(String filePath) {
        String lower = filePath.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || 
               lower.endsWith(".png") || lower.endsWith(".gif") || 
               lower.endsWith(".bmp") || lower.endsWith(".tiff");
    }

    // ✨ Format file size for display
    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.1f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    // ✨ Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Test with a sample file path
            new DocumentViewerDialog("sample_document.pdf").setVisible(true);
        });
    }
}
