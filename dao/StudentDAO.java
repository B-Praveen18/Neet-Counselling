package com.neet.dao;

import com.neet.db.DatabaseConnection;
import com.neet.model.Student;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {
    
    // ==================== AUTHENTICATION ====================
    public Student authenticateStudent(String username, String password) {
        String sql = "SELECT * FROM students WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ==================== REGISTRATION ====================
    public boolean registerStudent(Student student) {
        String sql = "INSERT INTO students (" +
                     "student_id, username, password, full_name, date_of_birth, gender, " +
                     "email, mobile_number, father_name, mother_name, permanent_address, " +
                     "neet_roll_number, neet_application_number, neet_score, neet_rank, neet_percentile, " +
                     "class_10_school, class_10_board, class_10_year, class_10_marks, " +
                     "class_12_school, class_12_board, class_12_year, class_12_marks, pcb_marks, " +
                     "aadhar_number, identity_proof_type, identity_number, " +
                     "category, state, registration_status, created_date) " +
                     "VALUES (student_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING', SYSDATE)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"student_id"})) {
            
            pstmt.setString(1, student.getUsername());
            pstmt.setString(2, student.getPassword());
            pstmt.setString(3, student.getFullName());
            
            // Date of Birth
            if (student.getDateOfBirth() != null) {
                pstmt.setDate(4, new java.sql.Date(student.getDateOfBirth().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            
            pstmt.setString(5, student.getGender());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getMobileNumber());
            pstmt.setString(8, student.getFatherName());
            pstmt.setString(9, student.getMotherName());
            pstmt.setString(10, student.getPermanentAddress());
            pstmt.setString(11, student.getNeetRollNumber());
            pstmt.setString(12, student.getNeetApplicationNumber());
            pstmt.setInt(13, student.getNeetScore());
            pstmt.setInt(14, student.getNeetRank());
            pstmt.setDouble(15, student.getNeetPercentile());  // ✅ NOW INSERTED
            
            // Class 10 details - ✅ NOW INSERTED
            pstmt.setString(16, student.getClass10School());
            pstmt.setString(17, student.getClass10Board());
            pstmt.setString(18, student.getClass10Year());
            pstmt.setString(19, student.getClass10Marks());
            
            // Class 12 details - ✅ NOW INSERTED
            pstmt.setString(20, student.getClass12School());
            pstmt.setString(21, student.getClass12Board());
            pstmt.setString(22, student.getClass12Year());
            pstmt.setString(23, student.getClass12Marks());
            pstmt.setString(24, student.getPcbMarks());
            
            pstmt.setString(25, student.getAadharNumber());
            pstmt.setString(26, student.getIdentityProofType());  // ✅ NOW INSERTED
            pstmt.setString(27, student.getIdentityNumber());     // ✅ NOW INSERTED
            pstmt.setString(28, student.getCategory());
            pstmt.setString(29, student.getStateDomicile());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✓ Student registered successfully");
                
                // ✅ GET the generated student_id
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                int studentId = 0;
                
                if (generatedKeys.next()) {
                    studentId = generatedKeys.getInt(1);
                    System.out.println("✓ New student ID: " + studentId);
                }
                
                // ✅ INSERT DOCUMENTS
                if (student.getDocuments() != null && !student.getDocuments().isEmpty()) {
                    insertStudentDocuments(studentId, student.getDocuments());
                }
                
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error registering student: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    
    // ==================== INSERT DOCUMENTS ====================
    private void insertStudentDocuments(int studentId, Map<String, String> documents) {
        String sql = "INSERT INTO student_documents " +
                     "(document_id, student_id, document_type, document_name, file_path, file_name, file_size) " +
                     "VALUES (doc_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (documents == null || documents.isEmpty()) {
                System.out.println("⚠️ No documents to insert");
                return;
            }
            
            System.out.println("\n=== INSERTING DOCUMENTS ===");
            System.out.println("Student ID: " + studentId);
            System.out.println("Total documents: " + documents.size());
            
            int docCount = 0;
            
            for (Map.Entry<String, String> entry : documents.entrySet()) {
                String docType = entry.getKey();        // e.g., "NEET_SCORECARD"
                String filePath = entry.getValue();
                
                if (filePath != null && !filePath.isEmpty()) {
                    File file = new File(filePath);
                    String fileName = file.getName();
                    long fileSize = file.length();
                    
                    // ✅ KEY FIX: docType stays with underscores for database constraint!
                    String displayName = docType.replace("_", " ");  // For document_name column
                    
                    pstmt.setInt(1, studentId);
                    pstmt.setString(2, docType);           // ✅ "NEET_SCORECARD" (NOT "NEET SCORECARD")
                    pstmt.setString(3, displayName);       // "NEET SCORECARD" (for readability)
                    pstmt.setString(4, filePath);
                    pstmt.setString(5, fileName);
                    pstmt.setLong(6, fileSize);
                    
                    pstmt.addBatch();
                    docCount++;
                    
                    System.out.println(" ✓ " + displayName + " = " + fileName + " (" + formatFileSize(fileSize) + ")");
                }
            }
            
            if (docCount > 0) {
                int[] results = pstmt.executeBatch();
                System.out.println("\n✓ Inserted " + results.length + " documents");
                System.out.println("=== DOCUMENTS COMPLETE ===\n");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Error inserting documents: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return String.format("%.2f %s", bytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    // ==================== GET STUDENT ====================
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("✓ Student extracted - ID: " + studentId + ", Name: " + rs.getString("full_name"));
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ==================== UPDATE PROFILE ====================
    public boolean updateStudentProfile(Student student) {
        String sql = "UPDATE students SET full_name = ?, email = ?, mobile_number = ?, permanent_address = ? WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getMobileNumber());
            pstmt.setString(4, student.getPermanentAddress());
            pstmt.setInt(5, student.getStudentId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // ==================== GET ALL STUDENTS ====================
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    // ==================== EXTRACT FROM RESULTSET ====================
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        
        try {
            // ✅ MANDATORY FIELDS (must exist)
            student.setStudentId(rs.getInt("student_id"));
            student.setUsername(rs.getString("username"));
            student.setPassword(rs.getString("password"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setMobileNumber(rs.getString("mobile_number"));
            student.setNeetRollNumber(rs.getString("neet_roll_number"));
            student.setNeetScore(rs.getInt("neet_score"));
            student.setNeetRank(rs.getInt("neet_rank"));
            student.setCategory(rs.getString("category"));
            student.setStateDomicile(rs.getString("state"));
            student.setPermanentAddress(rs.getString("permanent_address"));
            student.setAadharNumber(rs.getString("aadhar_number"));
            student.setRegistrationStatus(rs.getString("registration_status"));
            
        } catch (SQLException e) {
            System.out.println("❌ Error with mandatory field: " + e.getMessage());
            throw e;
        }
        
        // ✅ OPTIONAL FIELDS (use try-catch for each)
        try {
            student.setFatherName(rs.getString("father_name"));
        } catch (SQLException e) {
            student.setFatherName("N/A");
        }
        
        try {
            student.setMotherName(rs.getString("mother_name"));
        } catch (SQLException e) {
            student.setMotherName("N/A");
        }
        
        try {
            student.setNeetApplicationNumber(rs.getString("neet_application_number"));
        } catch (SQLException e) {
            student.setNeetApplicationNumber("N/A");
        }
        
        try {
            student.setNeetPercentile(rs.getDouble("neet_percentile"));
        } catch (SQLException e) {
            student.setNeetPercentile(0.0);
        }
        
        try {
            student.setChoiceLocked(rs.getString("choice_locked"));
        } catch (SQLException e) {
            student.setChoiceLocked("NO");
        }
        
        try {
            student.setAllotmentStatus(rs.getString("allotment_status"));
        } catch (SQLException e) {
            student.setAllotmentStatus("NOT_ALLOTTED");
        }
        
        try {
            student.setClass10School(rs.getString("class_10_school"));
        } catch (SQLException e) {
            student.setClass10School("N/A");
        }
        
        try {
            student.setClass10Board(rs.getString("class_10_board"));
        } catch (SQLException e) {
            student.setClass10Board("N/A");
        }
        
        try {
            student.setClass10Year(rs.getString("class_10_year"));
        } catch (SQLException e) {
            student.setClass10Year("N/A");
        }
        
        try {
            student.setClass10Marks(rs.getString("class_10_marks"));
        } catch (SQLException e) {
            student.setClass10Marks("N/A");
        }
        
        try {
            student.setClass12School(rs.getString("class_12_school"));
        } catch (SQLException e) {
            student.setClass12School("N/A");
        }
        
        try {
            student.setClass12Board(rs.getString("class_12_board"));
        } catch (SQLException e) {
            student.setClass12Board("N/A");
        }
        
        try {
            student.setClass12Year(rs.getString("class_12_year"));
        } catch (SQLException e) {
            student.setClass12Year("N/A");
        }
        
        try {
            student.setClass12Marks(rs.getString("class_12_marks"));
        } catch (SQLException e) {
            student.setClass12Marks("N/A");
        }
        
        try {
            student.setPcbMarks(rs.getString("pcb_marks"));
        } catch (SQLException e) {
            student.setPcbMarks("N/A");
        }
        
        try {
            student.setIdentityProofType(rs.getString("identity_proof_type"));
        } catch (SQLException e) {
            student.setIdentityProofType("N/A");
        }
        
        try {
            student.setIdentityNumber(rs.getString("identity_number"));
        } catch (SQLException e) {
            student.setIdentityNumber("N/A");
        }
        
        try {
            student.setGender(rs.getString("gender"));
        } catch (SQLException e) {
            student.setGender("N/A");
        }
        
        try {
            student.setDateOfBirth((Date) rs.getDate("date_of_birth"));
        } catch (SQLException e) {
            student.setDateOfBirth(null);
        }
        
        return student;
    }

//==================== LOCK CHOICES ====================
public boolean lockChoices(int studentId) {
 String sql = "UPDATE students SET choice_locked = 'YES' WHERE student_id = ?";
 
 try (Connection conn = DatabaseConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {
     
     pstmt.setInt(1, studentId);
     int result = pstmt.executeUpdate();
     
     if (result > 0) {
         System.out.println("✓ Choices locked for student " + studentId);
         return true;
     }
 } catch (SQLException e) {
     System.out.println("❌ Error locking choices: " + e.getMessage());
     e.printStackTrace();
 }
 return false;
}


}