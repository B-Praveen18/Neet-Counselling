package com.neet.dao;

import com.neet.db.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class PaymentDAO {

    /**
     * Create payment record when student is allotted
     */
    public boolean createPaymentRecord(int studentId, int allotmentId,
                                       int collegeId, int courseId, double amountDue) {
        String sql = "INSERT INTO student_payments (PAYMENT_ID, STUDENT_ID, COLLEGE_ID, " +
                "COURSE_ID, ALLOTMENT_ID, AMOUNT_DUE, PAYMENT_STATUS) " +
                "VALUES (payment_seq.NEXTVAL, ?, ?, ?, ?, ?, 'PENDING')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, collegeId);
            pstmt.setInt(3, courseId);
            pstmt.setInt(4, allotmentId);
            pstmt.setDouble(5, amountDue);

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                System.out.println("✓ Payment record created | Student: " + studentId +
                        " | Amount: ₹" + amountDue);
            }

            return success;

        } catch (SQLException e) {
            System.out.println("❌ Error creating payment record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get payment details for a specific allotment
     */
    public Map<String, Object> getPaymentDetails(int allotmentId) {
        Map<String, Object> details = new HashMap<>();
        String sql = "SELECT * FROM student_payments WHERE allotment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, allotmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                details.put("payment_id", rs.getInt("payment_id"));
                details.put("student_id", rs.getInt("student_id"));
                details.put("college_id", rs.getInt("college_id"));
                details.put("course_id", rs.getInt("course_id"));
                details.put("amount_due", rs.getDouble("amount_due"));
                details.put("payment_status", rs.getString("payment_status"));
                details.put("payment_date", rs.getDate("payment_date"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching payment details: " + e.getMessage());
        }

        return details;
    }

    /**
     * Mark payment as PAID
     */
    public boolean markPaymentAsPaid(int paymentId) {
        String sql = "UPDATE student_payments SET payment_status = 'PAID', " +
                "payment_date = SYSDATE WHERE payment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paymentId);
            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                System.out.println("✓ Payment marked as PAID | Payment ID: " + paymentId);
            }

            return success;

        } catch (SQLException e) {
            System.out.println("❌ Error marking payment as paid: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all pending payments for a college
     */
    public List<Map<String, Object>> getPendingPayments(int collegeId) {
        List<Map<String, Object>> payments = new ArrayList<>();
        String sql = "SELECT sp.*, s.full_name, c.course_name FROM student_payments sp " +
                "JOIN students s ON sp.student_id = s.student_id " +
                "JOIN courses c ON sp.course_id = c.course_id " +
                "WHERE sp.college_id = ? ORDER BY sp.payment_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> payment = new HashMap<>();
                payment.put("payment_id", rs.getInt("payment_id"));
                payment.put("student_id", rs.getInt("student_id"));
                payment.put("student_name", rs.getString("full_name"));
                payment.put("course_name", rs.getString("course_name"));
                payment.put("amount_due", rs.getDouble("amount_due"));
                payment.put("payment_status", rs.getString("payment_status"));
                payment.put("payment_date", rs.getDate("payment_date"));
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching pending payments: " + e.getMessage());
        }

        return payments;
    }

    /**
     * Get payment statistics for a college
     */
    public Map<String, Integer> getPaymentStatistics(int collegeId) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", 0);
        stats.put("pending", 0);
        stats.put("paid", 0);

        String sql = "SELECT payment_status, COUNT(*) as count FROM student_payments " +
                "WHERE college_id = ? GROUP BY payment_status";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();

            int total = 0;
            while (rs.next()) {
                String status = rs.getString("payment_status");
                int count = rs.getInt("count");
                total += count;

                if ("PENDING".equals(status)) {
                    stats.put("pending", count);
                } else if ("PAID".equals(status)) {
                    stats.put("paid", count);
                }
            }
            stats.put("total", total);

        } catch (SQLException e) {
            System.out.println("❌ Error fetching statistics: " + e.getMessage());
        }

        return stats;
    }

    /**
     * Get total amount collected
     */
    public double getTotalAmountCollected(int collegeId) {
        String sql = "SELECT NVL(SUM(amount_due), 0) as total FROM student_payments " +
                "WHERE college_id = ? AND payment_status = 'PAID'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching total amount: " + e.getMessage());
        }

        return 0.0;
    }
 // Add this NEW method to PaymentDAO.java
    public boolean createPaymentRecord(Connection conn, int studentId, int allotmentId,
                                       int collegeId, int courseId, double amountDue) {
        String sql = "INSERT INTO student_payments (PAYMENT_ID, STUDENT_ID, COLLEGE_ID, " +
                     "COURSE_ID, ALLOTMENT_ID, AMOUNT_DUE, PAYMENT_STATUS) " +
                     "VALUES (payment_seq.NEXTVAL, ?, ?, ?, ?, ?, 'PENDING')";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, collegeId);
            pstmt.setInt(3, courseId);
            pstmt.setInt(4, allotmentId);
            pstmt.setDouble(5, amountDue);
            
            boolean success = pstmt.executeUpdate() > 0;
            if (success) {
                System.out.println("✓ Payment record created | Student: " + studentId +
                                 " | Amount: ₹" + amountDue);
            }
            return success;
            
        } catch (SQLException e) {
            System.out.println("❌ Error creating payment record: " + e.getMessage());
            return false;
        }
    }
    /**
     * Create payment record using existing connection (for transactions)
     */



}
