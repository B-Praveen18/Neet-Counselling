package com.neet.model;

import java.util.Date;

/**
 * Course Model Class
 */
public class Course {
    private int courseId;
    private int collegeId;
    private String courseName;
    private String courseType;
    private int totalSeats;
    private int availableSeats;
    private double feesPerYear;
    private int durationYears;
    private int preferenceOrder;
    private String dateAdded; // or Date, depends on how you read from DB

    // For display purposes
    private String collegeName;
    
    // Constructors
    public Course() {
    }
    
    public Course(int collegeId, String courseName, String courseType, int totalSeats, double feesPerYear, int durationYears) {
        this.collegeId = collegeId;
        this.courseName = courseName;
        this.courseType = courseType;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.feesPerYear = feesPerYear;
        this.durationYears = durationYears;
    }
    
    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public int getCollegeId() { return collegeId; }
    public void setCollegeId(int collegeId) { this.collegeId = collegeId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getCourseType() { return courseType; }
    public void setCourseType(String courseType) { this.courseType = courseType; }
    
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public double getFeesPerYear() { return feesPerYear; }
    public void setFeesPerYear(double feesPerYear) { this.feesPerYear = feesPerYear; }
    
    public int getDurationYears() { return durationYears; }
    public void setDurationYears(int durationYears) { this.durationYears = durationYears; }
    
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    // --- All your other getters/setters first...

    public int getPreferenceOrder() {
        return preferenceOrder;
    }
    public void setPreferenceOrder(int preferenceOrder) {
        this.preferenceOrder = preferenceOrder;
    }

    public String getDateAdded() {
        return dateAdded;
    }
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    
    @Override
    public String toString() {
        return courseName + " - " + (collegeName != null ? collegeName : "College ID: " + collegeId);
    }
}


/**
 * Admin Model Class
 */
class Admin {
    private int adminId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private Date createdDate;
    
    public Admin() {
    }
    
    public Admin(String username, String password, String fullName, String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
    
    // Getters and Setters
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}


/**
 * SeatAllotment Model Class
 */
class SeatAllotment {
    private int allotmentId;
    private int studentId;
    private int collegeId;
    private int courseId;
    private int allotmentRound;
    private Date allotmentDate;
    private String acceptanceStatus;
    private String reportingStatus;
    private String allotmentLetterPath;
    
    // For display
    private String studentName;
    private String collegeName;
    private String courseName;
    
    public SeatAllotment() {
    }
    
    public SeatAllotment(int studentId, int collegeId, int courseId, int allotmentRound) {
        this.studentId = studentId;
        this.collegeId = collegeId;
        this.courseId = courseId;
        this.allotmentRound = allotmentRound;
        this.acceptanceStatus = "PENDING";
        this.reportingStatus = "NOT_REPORTED";
    }
    
    // Getters and Setters
    public int getAllotmentId() { return allotmentId; }
    public void setAllotmentId(int allotmentId) { this.allotmentId = allotmentId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getCollegeId() { return collegeId; }
    public void setCollegeId(int collegeId) { this.collegeId = collegeId; }
    
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public int getAllotmentRound() { return allotmentRound; }
    public void setAllotmentRound(int allotmentRound) { this.allotmentRound = allotmentRound; }
    
    public Date getAllotmentDate() { return allotmentDate; }
    public void setAllotmentDate(Date allotmentDate) { this.allotmentDate = allotmentDate; }
    
    public String getAcceptanceStatus() { return acceptanceStatus; }
    public void setAcceptanceStatus(String acceptanceStatus) { this.acceptanceStatus = acceptanceStatus; }
    
    public String getReportingStatus() { return reportingStatus; }
    public void setReportingStatus(String reportingStatus) { this.reportingStatus = reportingStatus; }
    
    public String getAllotmentLetterPath() { return allotmentLetterPath; }
    public void setAllotmentLetterPath(String allotmentLetterPath) { this.allotmentLetterPath = allotmentLetterPath; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}


/**
 * Choice Model Class
 */
class Choice {
    private int choiceId;
    private int studentId;
    private int collegeId;
    private int courseId;
    private int preferenceNumber;
    private int choiceRound;
    private Date createdDate;
    
    // For display
    private String collegeName;
    private String courseName;
    
    public Choice() {
    }
    
    public Choice(int studentId, int collegeId, int courseId, int preferenceNumber, int choiceRound) {
        this.studentId = studentId;
        this.collegeId = collegeId;
        this.courseId = courseId;
        this.preferenceNumber = preferenceNumber;
        this.choiceRound = choiceRound;
    }
    
    // Getters and Setters
    public int getChoiceId() { return choiceId; }
    public void setChoiceId(int choiceId) { this.choiceId = choiceId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getCollegeId() { return collegeId; }
    public void setCollegeId(int collegeId) { this.collegeId = collegeId; }
    
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public int getPreferenceNumber() { return preferenceNumber; }
    public void setPreferenceNumber(int preferenceNumber) { this.preferenceNumber = preferenceNumber; }
    
    public int getChoiceRound() { return choiceRound; }
    public void setChoiceRound(int choiceRound) { this.choiceRound = choiceRound; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}
