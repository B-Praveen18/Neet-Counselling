package com.neet.dao;

import com.neet.db.DatabaseConnection;
import com.neet.model.College;
import com.neet.model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for College operations - BACKWARD COMPATIBLE
 * Methods without throws for existing code (Student, Admin)
 * Methods with throws for new code (CollegeDashboard)
 */
public class CollegeDAO {
    
    // ========== METHODS WITHOUT THROWS (For StudentChoiceFrame & AdminDashboard) ==========
    
    /**
     * Authenticate college login (Silent error handling)
     */
    public College authenticateCollege(String username, String password) {
        String sql = "SELECT * FROM colleges WHERE username = ? AND password = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("✓ College authenticated: " + rs.getString("college_name"));
                return extractCollegeFromResultSet(rs);
            } else {
                System.out.println("✗ College authentication failed");
            }
        } catch (SQLException e) {
            System.err.println("Database error in authenticateCollege: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get college by ID (Silent error handling)
     */
    public College getCollegeById(int collegeId) {
        String sql = "SELECT * FROM colleges WHERE college_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("✓ College found: ID " + collegeId);
                return extractCollegeFromResultSet(rs);
            } else {
                System.err.println("✗ College not found: ID " + collegeId);
            }
        } catch (SQLException e) {
            System.err.println("Database error in getCollegeById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all active colleges (Silent error handling)
     */
    public List<College> getAllColleges() {
        List<College> colleges = new ArrayList<>();
        String sql = "SELECT * FROM colleges WHERE status = 'ACTIVE' ORDER BY college_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                colleges.add(extractCollegeFromResultSet(rs));
            }
            System.out.println("✓ Fetched " + colleges.size() + " active colleges");
        } catch (SQLException e) {
            System.err.println("Database error in getAllColleges: " + e.getMessage());
            e.printStackTrace();
        }
        return colleges;
    }
    
    /**
     * Get courses by college ID (Silent error handling - FOR StudentChoiceFrame)
     */
    public List<Course> getCoursesByCollegeId(int collegeId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, col.college_name FROM courses c " +
                    "JOIN colleges col ON c.college_id = col.college_id " +
                    "WHERE c.college_id = ? " +
                    "ORDER BY c.course_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                course.setCollegeName(rs.getString("college_name"));
                courses.add(course);
            }
            System.out.println("✓ Fetched " + courses.size() + " courses for college ID " + collegeId);
        } catch (SQLException e) {
            System.err.println("Database error in getCoursesByCollegeId: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }
    
    /**
     * Get all courses with college details (Silent error handling)
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, col.college_name FROM courses c " +
                    "JOIN colleges col ON c.college_id = col.college_id " +
                    "WHERE c.available_seats > 0 " +
                    "ORDER BY col.college_name, c.course_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                course.setCollegeName(rs.getString("college_name"));
                courses.add(course);
            }
            System.out.println("✓ Fetched " + courses.size() + " available courses");
        } catch (SQLException e) {
            System.err.println("Database error in getAllCourses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }
    
    /**
     * Search colleges by name or code (Silent error handling - FOR StudentChoiceFrame)
     */
    public List<College> searchCollegesByNameOrCode(String term) {
        if (term == null || term.trim().isEmpty()) {
            System.out.println("Empty search term provided");
            return new ArrayList<>();
        }
        
        List<College> colleges = new ArrayList<>();
        String sql = "SELECT * FROM colleges WHERE status = 'ACTIVE' AND " +
                     "(LOWER(college_name) LIKE ? OR LOWER(college_code) LIKE ?) " +
                     "ORDER BY college_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            String likeTerm = "%" + term.toLowerCase().trim() + "%";
            pst.setString(1, likeTerm);
            pst.setString(2, likeTerm);
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                colleges.add(extractCollegeFromResultSet(rs));
            }
            System.out.println("✓ Found " + colleges.size() + " colleges matching: " + term);
        } catch (SQLException e) {
            System.err.println("Database error in searchCollegesByNameOrCode: " + e.getMessage());
            e.printStackTrace();
        }
        return colleges;
    }
    
    /**
     * Update available seats (Silent error handling)
     */
    public boolean updateAvailableSeats(int courseId, int seats) {
        if (seats < 0) {
            System.err.println("Invalid seat count: " + seats);
            return false;
        }
        
        String sql = "UPDATE courses SET available_seats = ? WHERE course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seats);
            pstmt.setInt(2, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Available seats updated for course ID " + courseId + " to " + seats);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database error in updateAvailableSeats: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // ========== METHODS WITH THROWS (For NEW CollegeDashboard) ==========
    
    /**
     * Get college by ID with exception throwing
     * @throws SQLException if database error occurs
     */
    public College getCollegeByIdThrows(int collegeId) throws SQLException {
        String sql = "SELECT * FROM colleges WHERE college_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("✓ College found: ID " + collegeId);
                return extractCollegeFromResultSet(rs);
            }
        }
        return null;
    }
    
    /**
     * Get courses by college ID with exception throwing
     * @throws SQLException if database error occurs
     */
    public List<Course> getCoursesByCollegeIdThrows(int collegeId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, col.college_name FROM courses c " +
                    "JOIN colleges col ON c.college_id = col.college_id " +
                    "WHERE c.college_id = ? " +
                    "ORDER BY c.course_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                course.setCollegeName(rs.getString("college_name"));
                courses.add(course);
            }
        }
        return courses;
    }
    
    /**
     * Get course by ID with exception throwing
     * @throws SQLException if database error occurs
     */
    public Course getCourseById(int courseId) throws SQLException {
        String sql = "SELECT c.*, col.college_name FROM courses c " +
                     "JOIN colleges col ON c.college_id = col.college_id " +
                     "WHERE c.course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                course.setCollegeName(rs.getString("college_name"));
                System.out.println("✓ Course found: " + course.getCourseName());
                return course;
            }
        }
        return null;
    }
    
    /**
     * Add new course with exception throwing
     * @throws SQLException if database error occurs
     */
    public boolean addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (college_id, course_name, course_type, " +
                     "total_seats, available_seats, fees_per_year, duration_years) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, course.getCollegeId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getCourseType());
            pstmt.setInt(4, course.getTotalSeats());
            pstmt.setInt(5, course.getAvailableSeats());
            pstmt.setDouble(6, course.getFeesPerYear());
            pstmt.setInt(7, course.getDurationYears());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Course added: " + course.getCourseName());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Update available seats with exception throwing
     * @throws SQLException if database error occurs
     */
    public boolean updateAvailableSeatsThrows(int courseId, int seats) throws SQLException {
        if (seats < 0) {
            throw new IllegalArgumentException("Seats cannot be negative");
        }
        
        String sql = "UPDATE courses SET available_seats = ? WHERE course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, seats);
            pstmt.setInt(2, courseId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Available seats updated for course ID " + courseId + " to " + seats);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Decrease available seats when student enrolls with exception throwing
     * @throws SQLException if database error occurs
     */
    public boolean decreaseAvailableSeats(int courseId, int count) throws SQLException {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        String sql = "UPDATE courses SET available_seats = available_seats - ? " +
                     "WHERE course_id = ? AND available_seats >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, count);
            pstmt.setInt(2, courseId);
            pstmt.setInt(3, count);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Seats decreased for course ID " + courseId + " by " + count);
                return true;
            } else {
                System.err.println("✗ Insufficient seats available for course ID " + courseId);
            }
        }
        return false;
    }
    
    /**
     * Update college information with exception throwing
     * @throws SQLException if database error occurs
     */
    public boolean updateCollege(College college) throws SQLException {
        String sql = "UPDATE colleges SET college_name = ?, email = ?, phone = ?, " +
                     "state = ?, city = ?, status = ? WHERE college_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, college.getCollegeName());
            pstmt.setString(2, college.getEmail());
            pstmt.setString(3, college.getPhone());
            pstmt.setString(4, college.getState());
            pstmt.setString(5, college.getCity());
            pstmt.setString(6, college.getStatus());
            pstmt.setInt(7, college.getCollegeId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ College updated: " + college.getCollegeName());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get allotted students for a college with exception throwing
     * @throws SQLException if database error occurs
     */
    public List<Object[]> getAllottedStudentsByCollege(int collegeId) throws SQLException {
        List<Object[]> students = new ArrayList<>();
        String sql = "SELECT sa.allotment_id, s.student_id, s.full_name, s.neet_roll_no, " +
                     "s.neet_score, s.neet_rank, s.category, c.course_name, sa.allotment_status " +
                     "FROM seat_allotments sa " +
                     "JOIN students s ON sa.student_id = s.student_id " +
                     "JOIN courses c ON sa.course_id = c.course_id " +
                     "WHERE c.college_id = ? AND sa.allotment_status = 'ALLOTTED' " +
                     "ORDER BY s.neet_rank ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(new Object[]{
                    rs.getInt("allotment_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("neet_roll_no"),
                    rs.getInt("neet_score"),
                    rs.getInt("neet_rank"),
                    rs.getString("category"),
                    rs.getString("course_name"),
                    rs.getString("allotment_status")
                });
            }
        }
        return students;
    }
    
    /**
     * Get document verification pending students with exception throwing
     * @throws SQLException if database error occurs
     */
    public List<Object[]> getPendingDocumentVerification(int collegeId) throws SQLException {
        List<Object[]> students = new ArrayList<>();
        String sql = "SELECT dv.doc_verification_id, s.student_id, s.full_name, s.neet_roll_no, " +
                     "dv.doc_10th, dv.doc_12th, dv.doc_neet, dv.doc_aadhar, s.category, " +
                     "dv.verification_status " +
                     "FROM document_verification dv " +
                     "JOIN students s ON dv.student_id = s.student_id " +
                     "JOIN seat_allotments sa ON s.student_id = sa.student_id " +
                     "JOIN courses c ON sa.course_id = c.course_id " +
                     "WHERE c.college_id = ? AND dv.verification_status = 'PENDING' " +
                     "ORDER BY s.neet_rank ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(new Object[]{
                    rs.getInt("doc_verification_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("neet_roll_no"),
                    rs.getBoolean("doc_10th"),
                    rs.getBoolean("doc_12th"),
                    rs.getBoolean("doc_neet"),
                    rs.getBoolean("doc_aadhar"),
                    rs.getString("category"),
                    rs.getString("verification_status")
                });
            }
        }
        return students;
    }
    
    /**
     * Update document verification status with exception throwing
     * @throws SQLException if database error occurs
     */
    public boolean updateDocumentVerification(int docVerificationId, String status) throws SQLException {
        String sql = "UPDATE document_verification SET verification_status = ?, verified_date = CURDATE() " +
                     "WHERE doc_verification_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, docVerificationId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✓ Document verification updated: ID " + docVerificationId + " -> " + status);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get admitted students (fee paid) with exception throwing
     * @throws SQLException if database error occurs
     */
    public List<Object[]> getAdmittedStudentsByCollege(int collegeId) throws SQLException {
        List<Object[]> students = new ArrayList<>();
        String sql = "SELECT fp.payment_id, s.student_id, s.full_name, s.neet_roll_no, " +
                     "c.course_name, fp.amount_paid, fp.payment_date, adm.admission_status " +
                     "FROM fee_payments fp " +
                     "JOIN students s ON fp.student_id = s.student_id " +
                     "JOIN seat_allotments sa ON s.student_id = sa.student_id " +
                     "JOIN courses c ON sa.course_id = c.course_id " +
                     "JOIN admissions adm ON s.student_id = adm.student_id " +
                     "WHERE c.college_id = ? AND fp.payment_status = 'COMPLETED' " +
                     "ORDER BY fp.payment_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(new Object[]{
                    rs.getInt("payment_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("neet_roll_no"),
                    rs.getString("course_name"),
                    "₹ " + String.format("%.2f", rs.getDouble("amount_paid")),
                    rs.getDate("payment_date"),
                    rs.getString("admission_status")
                });
            }
        }
        return students;
    }
    
    /**
     * Get total fees collected for a college with exception throwing
     * @throws SQLException if database error occurs
     */
    public double getTotalFeesCollected(int collegeId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(fp.amount_paid), 0) as total_fees " +
                     "FROM fee_payments fp " +
                     "JOIN students s ON fp.student_id = s.student_id " +
                     "JOIN seat_allotments sa ON s.student_id = sa.student_id " +
                     "JOIN courses c ON sa.course_id = c.course_id " +
                     "WHERE c.college_id = ? AND fp.payment_status = 'COMPLETED'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                double totalFees = rs.getDouble("total_fees");
                System.out.println("✓ Total fees collected for college ID " + collegeId + ": ₹" + totalFees);
                return totalFees;
            }
        }
        return 0.0;
    }
    
    /**
     * Extract College object from ResultSet
     */
    private College extractCollegeFromResultSet(ResultSet rs) throws SQLException {
        College college = new College();
        college.setCollegeId(rs.getInt("college_id"));
        college.setCollegeCode(rs.getString("college_code"));
        college.setCollegeName(rs.getString("college_name"));
        college.setUsername(rs.getString("username"));
        college.setPassword(rs.getString("password"));
        college.setCollegeType(rs.getString("college_type"));
        college.setState(rs.getString("state"));
        college.setCity(rs.getString("city"));
        college.setEmail(rs.getString("email"));
        college.setPhone(rs.getString("phone"));
        college.setTotalSeats(rs.getInt("total_seats"));
        college.setAvailableSeats(rs.getInt("available_seats"));
        college.setStatus(rs.getString("status"));
        college.setCreatedDate(rs.getDate("created_date"));
        return college;
    }
    
    /**
     * Extract Course object from ResultSet
     */
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCollegeId(rs.getInt("college_id"));
        course.setCourseName(rs.getString("course_name"));
        course.setCourseType(rs.getString("course_type"));
        course.setTotalSeats(rs.getInt("total_seats"));
        course.setAvailableSeats(rs.getInt("available_seats"));
        course.setFeesPerYear(rs.getDouble("fees_per_year"));
        course.setDurationYears(rs.getInt("duration_years"));
        return course;
    }
}
