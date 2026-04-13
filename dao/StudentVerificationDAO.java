package com.neet.dao;

import com.neet.db.DatabaseConnection;
import com.neet.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class StudentVerificationDAO {

    public List getAllStudentsForVerification() {
        List students = new ArrayList<>();
        String sql = "SELECT s.*, vd.verification_status FROM students s " +
                     "LEFT JOIN verification_details vd ON s.student_id = vd.student_id " +
                     "ORDER BY s.neet_rank";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    public List getStudentsByStatus(String status) {
        List students = new ArrayList<>();
        String sql = "SELECT s.* FROM students s " +
                     "LEFT JOIN verification_details vd ON s.student_id = vd.student_id " +
                     "WHERE vd.verification_status = ? OR (vd.verification_status IS NULL AND ? = 'PENDING') " +
                     "ORDER BY s.neet_rank";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = extractStudentFromResultSet(rs);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * ✅ FIXED: Returns file_path along with other document info
     * ✅ NEW: Validates file existence before returning
     * ✅ NEW: Uses relative path if absolute path doesn't exist
     */
    public List getStudentDocuments(int studentId) {
        List documents = new ArrayList<>();
        String sql = "SELECT document_id, document_type, file_path, file_name, upload_date " +
                     "FROM student_documents WHERE student_id = ? ORDER BY upload_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("\n=== Fetching documents for Student " + studentId);
            int count = 0;
            
            while (rs.next()) {
                String docType = rs.getString("document_type");
                String fileName = rs.getString("file_name");
                String filePath = rs.getString("file_path");
                String uploadDate = rs.getDate("upload_date").toString();
                
                // ✅ NEW: Validate and correct file path if needed
                String validatedPath = validateAndFixFilePath(filePath, fileName, studentId);
                
                System.out.println("✓ Document " + (++count) + ": " + docType + 
                                 " | File: " + fileName + " | Path: " + validatedPath);
                
                // ✅ Return 4 values including validated file_path
                String[] doc = new String[]{docType, fileName, uploadDate, validatedPath};
                documents.add(doc);
            }
            
            System.out.println("✓ Total documents found: " + documents.size() + "\n");
            
        } catch (SQLException e) {
            System.out.println("❌ Error fetching documents: " + e.getMessage());
            e.printStackTrace();
        }
        
        return documents;
    }

    /**
     * ✅ NEW METHOD: Validate file path and return correct path
     * If file doesn't exist at stored path, try relative paths
     */
    private String validateAndFixFilePath(String storedPath, String fileName, int studentId) {
        if (storedPath == null || storedPath.isEmpty()) {
            return null;
        }
        
        // Check 1: File exists at stored path
        File file = new File(storedPath);
        if (file.exists()) {
            return storedPath;
        }
        
        System.out.println("⚠️ File not found at: " + storedPath);
        
        // Check 2: Try project root documents folder
        String projectPath = System.getProperty("user.dir");
        String projectDocPath = projectPath + File.separator + "documents" + 
                               File.separator + "student_" + studentId + 
                               File.separator + fileName;
        file = new File(projectDocPath);
        if (file.exists()) {
            System.out.println("✓ Found at project path: " + projectDocPath);
            return projectDocPath;
        }
        
        // Check 3: Try user home documents
        String userDocsPath = System.getProperty("user.home") + File.separator + "NEETDocs" + 
                             File.separator + "student_" + studentId + 
                             File.separator + fileName;
        file = new File(userDocsPath);
        if (file.exists()) {
            System.out.println("✓ Found at user docs path: " + userDocsPath);
            return userDocsPath;
        }
        
        // If file not found anywhere, return original path (DocumentViewerDialog will show error)
        System.out.println("❌ Could not locate file at any known path");
        return storedPath;
    }

    // Lock choices
    public boolean lockChoices(int studentId) {
        String sql = "UPDATE students SET choice_locked = 'YES' WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean approveStudent(int studentId, int adminId, String comments) {
        String sql = "INSERT INTO verification_details " +
                     "(verification_id, student_id, admin_id, verification_status, comments, verified_date, verified_by) " +
                     "VALUES (verify_seq.NEXTVAL, ?, ?, 'APPROVED', ?, SYSDATE, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, adminId);
            pstmt.setString(3, comments);
            pstmt.setString(4, "ADMIN_" + adminId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                updateStudentRegistrationStatus(studentId, "VERIFIED", conn);
            }
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error approving student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean rejectStudent(int studentId, int adminId, String reason) {
        String sql = "INSERT INTO verification_details " +
                     "(verification_id, student_id, admin_id, verification_status, rejection_reason, verified_date, verified_by) " +
                     "VALUES (verify_seq.NEXTVAL, ?, ?, 'REJECTED', ?, SYSDATE, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, adminId);
            pstmt.setString(3, reason);
            pstmt.setString(4, "ADMIN_" + adminId);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                updateStudentRegistrationStatus(studentId, "REJECTED", conn);
            }
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error rejecting student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String getVerificationStatus(int studentId) {
        String sql = "SELECT verification_status FROM verification_details WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("verification_status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "PENDING";
    }

    private boolean updateStudentRegistrationStatus(int studentId, String status, Connection conn) {
        String sql = "UPDATE students SET registration_status = ? WHERE student_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setUsername(rs.getString("username"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setMobileNumber(rs.getString("mobile_number"));
        student.setNeetRollNumber(rs.getString("neet_roll_number"));
        student.setNeetRank(rs.getInt("neet_rank"));
        student.setNeetScore(rs.getInt("neet_score"));
        student.setCategory(rs.getString("category"));
        student.setRegistrationStatus(rs.getString("registration_status"));
        return student;
    }

    public String getStudentDocumentPath(int studentId, String documentType) {
        List<String[]> docs = getStudentDocuments(studentId);
        for (String[] doc : docs) {
            if (doc[0].equalsIgnoreCase(documentType)) {
                return doc[3]; // doc[3] is filePath by your code
            }
        }
        return null;
    }
}
