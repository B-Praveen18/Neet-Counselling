package com.neet.dao;

import com.neet.db.DatabaseConnection;
import com.neet.model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentChoiceDAO {

    public boolean addChoice(int studentId, int collegeId, int courseId, int preferenceOrder) {
        String sql = "INSERT INTO student_choices (choice_id, student_id, college_id, course_id, preference_order, created_date) " +
                "VALUES (choice_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, collegeId);
            pstmt.setInt(3, courseId);
            pstmt.setInt(4, preferenceOrder);

            System.out.println("Adding choice for student " + studentId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding choice: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List getStudentChoices(int studentId) {
        List courses = new ArrayList<>();
        String sql = "SELECT c.course_id, col.college_name, c.course_name, c.course_type, c.available_seats, c.fees_per_year, sc.preference_order, sc.created_date " +
                "FROM student_choices sc " +
                "JOIN courses c ON sc.course_id = c.course_id " +
                "JOIN colleges col ON sc.college_id = col.college_id " +
                "WHERE sc.student_id = ? " +
                "ORDER BY sc.preference_order";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setCourseType(rs.getString("course_type"));
                course.setAvailableSeats(rs.getInt("available_seats"));
                course.setFeesPerYear(rs.getDouble("fees_per_year"));
                course.setCollegeName(rs.getString("college_name"));
                course.setPreferenceOrder(rs.getInt("preference_order"));
                course.setDateAdded(rs.getString("created_date"));
                courses.add(course);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving student choices: " + e.getMessage());
            e.printStackTrace();
        }

        return courses;
    }

    public boolean clearAllChoices(int studentId) {
        String sql = "DELETE FROM student_choices WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() >= 0;

        } catch (SQLException e) {
            System.out.println("Error clearing choices: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getChoiceCount(int studentId) {
        String sql = "SELECT COUNT(*) FROM student_choices WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ==================== DELETE BY PREFERENCE ORDER ====================
    public boolean deleteChoice(int studentId, int preferenceOrder) {
        String sql = "DELETE FROM student_choices WHERE student_id = ? AND preference_order = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, preferenceOrder);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("✓ Choice deleted - Student: " + studentId + ", Preference: " + preferenceOrder);
                return true;
            }

            System.out.println("⚠️ No choice found to delete - Student: " + studentId + ", Preference: " + preferenceOrder);
            return false;

        } catch (SQLException e) {
            System.out.println("❌ Error deleting choice: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ ALIAS METHOD - StudentDashboard calls this method name
    public void deleteStudentChoice(int studentId, int preferenceOrder) {
        deleteChoice(studentId, preferenceOrder);
    }

    // ==================== DELETE BY COURSE ID (ALTERNATIVE) ====================
    public boolean deleteChoiceByCourseId(int studentId, int courseId) {
        String sql = "DELETE FROM student_choices WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("✓ Choice deleted by course - Student: " + studentId + ", Course: " + courseId);
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("❌ Error deleting choice by course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ CHECK IF PREFERENCE ALREADY EXISTS FOR THIS STUDENT
    public boolean preferenceExists(int studentId, int preferenceOrder) {
        String sql = "SELECT COUNT(*) FROM student_choices WHERE student_id = ? AND preference_order = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, preferenceOrder);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking preference: " + e.getMessage());
        }

        return false;
    }
}
