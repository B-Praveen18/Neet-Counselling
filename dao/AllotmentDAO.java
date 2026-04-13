package com.neet.dao;

import com.neet.db.DatabaseConnection;
import java.sql.*;
import java.util.*;

/**
 * Allotment DAO - Fixed Version with Fair Algorithm
 * ✨ FEATURES:
 * - Round 1: Processes ranks 1-20
 * - Round 2: Processes (1-20 WAITLIST) + (21-40 NEW)
 * - Round 3: Processes (1-40 WAITLIST) + (41-60 NEW)
 * - Round sequence validation
 * - Auto-commit safe
 * - NO waitlist record insertion (only marks status)
 */
public class AllotmentDAO {

    // Define rank ranges for each round
    private static final int ROUND1_MIN = 1;
    private static final int ROUND1_MAX = 20;
    private static final int ROUND2_MIN = 21;
    private static final int ROUND2_MAX = 40;
    private static final int ROUND3_MIN = 41;
    private static final int ROUND3_MAX = 60;
    private static final int SEATS_TO_REDUCE = 20;

    // ===== RUN ALLOTMENT FOR A SPECIFIC ROUND =====
    public boolean runAllotmentRound(int roundNumber) {
        System.out.println("🔄 Starting Round " + roundNumber + " Allotment...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Get students for this round (includes waitlisted if applicable)
            List<StudentAllotmentData> students = getStudentsForRound(conn, roundNumber);
            
            if (students.isEmpty()) {
                System.out.println("❌ No students eligible for Round " + roundNumber);
                conn.rollback();
                return false;
            }

            System.out.println("📊 Processing " + students.size() + " students for Round " + roundNumber);

            int allotmentsCount = 0;
            for (StudentAllotmentData student : students) {
                boolean allotted = processStudentAllotment(conn, student, roundNumber);
                if (allotted) allotmentsCount++;
            }

            conn.commit();
            System.out.println("✅ Round " + roundNumber + " Allotment Complete!");
            System.out.println("   Total Allotted: " + allotmentsCount + " / " + students.size());

            return true;
        } catch (Exception e) {
            System.out.println("Error running allotment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===== GET STUDENTS FOR SPECIFIC ROUND (WITH WAITLIST SUPPORT) =====
    private List<StudentAllotmentData> getStudentsForRound(Connection conn, int roundNumber) throws SQLException {
        List<StudentAllotmentData> students = new ArrayList<>();

        if (roundNumber == 1) {
            // ===== ROUND 1: Process only ranks 1-20 =====
            String sql = "SELECT s.student_id, s.neet_rank, s.category, s.full_name " +
                        "FROM students s " +
                        "WHERE s.registration_status = 'VERIFIED' " +
                        "AND s.choice_locked = 'YES' " +
                        "AND s.neet_rank BETWEEN 1 AND 20 " +
                        "AND (s.allotment_status IS NULL OR s.allotment_status = 'WAITLIST' OR s.allotment_status = 'NOT_ALLOTTED') " +
                        "ORDER BY s.neet_rank ASC";
            
            students.addAll(executeStudentQuery(conn, sql));
            System.out.println("📊 Round 1: Processing ranks 1-20 only");

        } else if (roundNumber == 2) {
            // ===== ROUND 2: Process (1-20 WAITLIST) + (21-40 NEW) =====
            
            // PART A: Get waitlisted students from ranks 1-20
            String sqlWaitlist = "SELECT s.student_id, s.neet_rank, s.category, s.full_name " +
                                "FROM students s " +
                                "WHERE s.registration_status = 'VERIFIED' " +
                                "AND s.choice_locked = 'YES' " +
                                "AND s.neet_rank BETWEEN 1 AND 20 " +
                                "AND s.allotment_status = 'WAITLIST' " +
                                "ORDER BY s.neet_rank ASC";
            
            List<StudentAllotmentData> waitlistedR1 = executeStudentQuery(conn, sqlWaitlist);
            students.addAll(waitlistedR1);
            System.out.println("📊 Round 2, Part A: Processing " + waitlistedR1.size() + " waitlisted from ranks 1-20");
            
            // PART B: Get new eligible students from ranks 21-40
            String sqlNewRanks = "SELECT s.student_id, s.neet_rank, s.category, s.full_name " +
                                "FROM students s " +
                                "WHERE s.registration_status = 'VERIFIED' " +
                                "AND s.choice_locked = 'YES' " +
                                "AND s.neet_rank BETWEEN 21 AND 40 " +
                                "AND (s.allotment_status IS NULL OR s.allotment_status = 'WAITLIST' OR s.allotment_status = 'NOT_ALLOTTED') " +
                                "ORDER BY s.neet_rank ASC";
            
            List<StudentAllotmentData> newStudentsR2 = executeStudentQuery(conn, sqlNewRanks);
            students.addAll(newStudentsR2);
            System.out.println("📊 Round 2, Part B: Processing " + newStudentsR2.size() + " new students from ranks 21-40");

        } else if (roundNumber == 3) {
            // ===== ROUND 3: Process (1-40 WAITLIST) + (41-60 NEW) =====
            
            // PART A: Get waitlisted students from ranks 1-40
            String sqlWaitlist = "SELECT s.student_id, s.neet_rank, s.category, s.full_name " +
                                "FROM students s " +
                                "WHERE s.registration_status = 'VERIFIED' " +
                                "AND s.choice_locked = 'YES' " +
                                "AND s.neet_rank BETWEEN 1 AND 40 " +
                                "AND s.allotment_status = 'WAITLIST' " +
                                "ORDER BY s.neet_rank ASC";
            
            List<StudentAllotmentData> waitlistedR1R2 = executeStudentQuery(conn, sqlWaitlist);
            students.addAll(waitlistedR1R2);
            System.out.println("📊 Round 3, Part A: Processing " + waitlistedR1R2.size() + " waitlisted from ranks 1-40");
            
            // PART B: Get new eligible students from ranks 41-60
            String sqlNewRanks = "SELECT s.student_id, s.neet_rank, s.category, s.full_name " +
                                "FROM students s " +
                                "WHERE s.registration_status = 'VERIFIED' " +
                                "AND s.choice_locked = 'YES' " +
                                "AND s.neet_rank BETWEEN 41 AND 60 " +
                                "AND (s.allotment_status IS NULL OR s.allotment_status = 'WAITLIST' OR s.allotment_status = 'NOT_ALLOTTED') " +
                                "ORDER BY s.neet_rank ASC";
            
            List<StudentAllotmentData> newStudentsR3 = executeStudentQuery(conn, sqlNewRanks);
            students.addAll(newStudentsR3);
            System.out.println("📊 Round 3, Part B: Processing " + newStudentsR3.size() + " new students from ranks 41-60");
        }

        return students;
    }

    // ===== HELPER METHOD: Execute query and get student list =====
    private List<StudentAllotmentData> executeStudentQuery(Connection conn, String sql) throws SQLException {
        List<StudentAllotmentData> students = new ArrayList<>();
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StudentAllotmentData data = new StudentAllotmentData();
                data.studentId = rs.getInt("student_id");
                data.neetRank = rs.getInt("neet_rank");
                data.category = rs.getString("category");
                data.fullName = rs.getString("full_name");
                students.add(data);
            }
        }
        
        return students;
    }

    // ===== PROCESS STUDENT ALLOTMENT =====
    private boolean processStudentAllotment(Connection conn, StudentAllotmentData student, int roundNumber) throws SQLException {
        String choiceSql = "SELECT sc.college_id, sc.course_id, sc.preference_order " +
                           "FROM student_choices sc " +
                           "WHERE sc.student_id = ? " +
                           "ORDER BY sc.preference_order ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(choiceSql)) {
            pstmt.setInt(1, student.studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int collegeId = rs.getInt("college_id");
                int courseId = rs.getInt("course_id");
                int prefOrder = rs.getInt("preference_order");

                // Check if seat is available
                if (isSeatAvailable(conn, collegeId, courseId)) {
                    // Allocate seat
                    if (allocateSeat(conn, student.studentId, collegeId, courseId, roundNumber, prefOrder)) {
                        // Decrease available seats
                        decreaseAvailableSeats(conn, courseId, collegeId, SEATS_TO_REDUCE);
                        updateCollegeAvailableSeats(conn, collegeId, SEATS_TO_REDUCE);
                        
                        // Get the allotment ID that was just created
                        int allotmentId = getLatestAllotmentId(conn, student.studentId);
                        
                        // Update student status to 'ALLOTTED'
                        updateStudentToAllotted(conn, student.studentId, collegeId, courseId, allotmentId);
                        
                        System.out.println("  ✓ Rank " + student.neetRank + " → Allotted to College " + collegeId + ", Course " + courseId);
                        return true;
                    }
                }
            }

            // ✅ If no preference matched, just mark as WAITLIST (NO record insertion)
            markAsWaitlist(conn, student.studentId);
            System.out.println("  ⏳ Rank " + student.neetRank + " → Added to WAITLIST");
            return false;
        }
    }

    // ===== GET LATEST ALLOTMENT ID =====
    private int getLatestAllotmentId(Connection conn, int studentId) throws SQLException {
        String sql = "SELECT allotment_id FROM student_allotment " +
                     "WHERE student_id = ? " +
                     "ORDER BY allotment_id DESC";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("allotment_id");
            }
        }
        
        return -1;
    }

    // ===== CHECK IF SEAT IS AVAILABLE =====
    private boolean isSeatAvailable(Connection conn, int collegeId, int courseId) throws SQLException {
        String sql = "SELECT available_seats FROM courses WHERE course_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int available = rs.getInt("available_seats");
                return available >= SEATS_TO_REDUCE;
            }
            
            return false;
        }
    }

    // ===== ALLOCATE SEAT =====
    private boolean allocateSeat(Connection conn, int studentId, int collegeId, int courseId,
                                 int roundNumber, int prefOrder) throws SQLException {
        String sql = "INSERT INTO student_allotment (allotment_id, student_id, college_id, course_id, allotment_round, preference_order, allotment_date, acceptance_status) " +
                    "VALUES (student_allotment_seq.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, 'PENDING')";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, collegeId);
            pstmt.setInt(3, courseId);
            pstmt.setInt(4, roundNumber);
            pstmt.setInt(5, prefOrder);
            return pstmt.executeUpdate() > 0;
        }
    }

    // ===== DECREASE AVAILABLE SEATS IN COURSES =====
    private void decreaseAvailableSeats(Connection conn, int courseId, int collegeId, int seatsToReduce) throws SQLException {
        String sql = "UPDATE courses SET available_seats = available_seats - ? WHERE course_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatsToReduce);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();
        }
    }

    // ===== UPDATE COLLEGE AVAILABLE SEATS =====
    private void updateCollegeAvailableSeats(Connection conn, int collegeId, int seatsToReduce) throws SQLException {
        String sql = "UPDATE colleges SET available_seats = available_seats - ? WHERE college_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatsToReduce);
            pstmt.setInt(2, collegeId);
            pstmt.executeUpdate();
        }
    }

    // ===== UPDATE STUDENT TO ALLOTTED =====
    private boolean updateStudentToAllotted(Connection conn, int studentId,
            int collegeId, int courseId, int allotmentId) {
        String sql = "UPDATE students SET allotment_status = 'ALLOTTED' WHERE student_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();

            // Create payment record automatically
            PaymentDAO paymentDAO = new PaymentDAO();

            // Get course fees
            String feesSql = "SELECT fees_per_year FROM courses WHERE course_id = ?";
            try (PreparedStatement feeStmt = conn.prepareStatement(feesSql)) {
                feeStmt.setInt(1, courseId);
                ResultSet rs = feeStmt.executeQuery();
                double amountDue = 0;
                if (rs.next()) {
                    amountDue = rs.getDouble("fees_per_year");
                }

                // Create payment record using existing connection
                paymentDAO.createPaymentRecord(conn, studentId, allotmentId, collegeId, courseId, amountDue);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== MARK STUDENT AS WAITLIST =====
    private void markAsWaitlist(Connection conn, int studentId) throws SQLException {
        String sql = "UPDATE students SET allotment_status = 'WAITLIST' WHERE student_id = ? AND allotment_status IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
        }
    }

    // ===== GET ALLOTMENT FOR A STUDENT =====
    public Map<String, String> getStudentAllotment(int studentId) {
        Map<String, String> allotment = new HashMap<>();
        String sql = "SELECT sa.allotment_id, sa.allotment_round, c.college_name, co.course_name, " +
                    "sa.acceptance_status, sa.allotment_date " +
                    "FROM student_allotment sa " +
                    "JOIN colleges c ON sa.college_id = c.college_id " +
                    "JOIN courses co ON sa.course_id = co.course_id " +
                    "WHERE sa.student_id = ? " +
                    "ORDER BY sa.allotment_round DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                allotment.put("round", String.valueOf(rs.getInt("allotment_round")));
                allotment.put("college", rs.getString("college_name"));
                allotment.put("course", rs.getString("course_name"));
                allotment.put("status", rs.getString("acceptance_status"));
                allotment.put("date", rs.getString("allotment_date"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching allotment: " + e.getMessage());
        }

        return allotment;
    }

    // ===== GET ALLOTMENT STATISTICS =====
    public Map<String, Integer> getAllotmentStats(int roundNumber) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT COUNT(*) AS total_allotted, " +
                    "SUM(CASE WHEN acceptance_status = 'ACCEPTED' THEN 1 ELSE 0 END) AS accepted, " +
                    "SUM(CASE WHEN acceptance_status = 'REJECTED' THEN 1 ELSE 0 END) AS rejected, " +
                    "SUM(CASE WHEN acceptance_status = 'PENDING' THEN 1 ELSE 0 END) AS pending " +
                    "FROM student_allotment WHERE allotment_round = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roundNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.put("total", rs.getInt("total_allotted"));
                stats.put("accepted", rs.getInt("accepted"));
                stats.put("rejected", rs.getInt("rejected"));
                stats.put("pending", rs.getInt("pending"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching stats: " + e.getMessage());
        }

        return stats;
    }

    // ===== UPDATE STUDENT ACCEPTANCE =====
    public boolean updateStudentAcceptance(int studentId, String status) {
        String sql = "UPDATE student_allotment SET acceptance_status = ? WHERE student_id = ? AND acceptance_status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, studentId);
            int updated = pstmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating acceptance: " + e.getMessage());
            return false;
        }
    }

    // ===== ROUND SEQUENCE VALIDATION =====
    public boolean isRoundAllowed(int requestedRound) {
        try {
            String sql = "SELECT current_round FROM admin_settings WHERE setting_id = 1";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                ResultSet rs = stmt.executeQuery(sql);
                
                if (!rs.next()) {
                    System.out.println("❌ ERROR: Admin settings not found!");
                    return false;
                }
                
                int currentRound = rs.getInt("current_round");
                
                if (requestedRound != currentRound) {
                    System.out.println("❌ ═══════════════════════════════════════════════════════════");
                    System.out.println("❌ ROUND SEQUENCE VIOLATION!");
                    System.out.println("❌ ───────────────────────────────────────────────────────────");
                    System.out.println("❌ Current Round: " + currentRound);
                    System.out.println("❌ Requested Round: " + requestedRound);
                    System.out.println("❌ ───────────────────────────────────────────────────────────");
                    
                    if (requestedRound < currentRound) {
                        System.out.println("❌ ERROR: Cannot go back to Round " + requestedRound + "!");
                        System.out.println("❌ You are already at Round " + currentRound + ".");
                    } else {
                        System.out.println("❌ ERROR: Cannot skip to Round " + requestedRound + "!");
                        System.out.println("❌ You must complete Round " + currentRound + " first.");
                    }
                    
                    System.out.println("❌ ═══════════════════════════════════════════════════════════");
                    return false;
                }
                
                System.out.println("✅ Round " + requestedRound + " is allowed!");
                return true;
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== COMPLETE ROUND =====
    public void completeRound(int round) {
        try {
            if (round < 1 || round > 3) {
                System.out.println("❌ Invalid round!");
                return;
            }
            
            if (round < 3) {
                String sql = "UPDATE admin_settings SET current_round = ? WHERE setting_id = 1";
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    
                    pstmt.setInt(1, round + 1);
                    pstmt.executeUpdate();
                    // No commit needed - auto-commit is enabled
                    
                    System.out.println("✅ Round " + round + " completed!");
                    System.out.println("✅ Now you can run Round " + (round + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== RESET TO ROUND 1 =====
    public void resetToRound1() {
        try {
            String sql = "UPDATE admin_settings SET current_round = 1 WHERE setting_id = 1";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                stmt.executeUpdate(sql);
                // No commit needed - auto-commit is enabled
                
                System.out.println("✅ Reset to Round 1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== INNER CLASS FOR STUDENT DATA =====
    private static class StudentAllotmentData {
        int studentId;
        int neetRank;
        String category;
        String fullName;
    }
}
