package com.neet.model;

import java.util.Date;
import java.util.Map;

/**
 * Student Model Class - Updated for NTA NEET Counselling 2025
 * Contains all fields from the new registration form
 */
public class Student {
    
    // Existing basic fields
    private int studentId;
    private String username;
    private String password;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String email;
    private String phone;
    private String address;
    
    // New Personal Information
    private String fatherName;
    private String motherName;
    private String mobileNumber;
    private String permanentAddress;
    
    // NEET Score Details
    private String neetRollNumber;
    private String neetApplicationNumber;
    private int neetScore;
    private int neetRank;
    private double neetPercentile;
    
    // Academic Details - Class 10
    private String class10School;
    private String class10Board;
    private String class10Year;
    private String class10Marks;
    
    // Academic Details - Class 12
    private String class12School;
    private String class12Board;
    private String class12Year;
    private String class12Marks;
    private String pcbMarks;
    
    // Identity Proof
    private String aadharNumber;
    private String identityProofType;
    private String identityNumber;
    
    // Category & Domicile
    private String category;
    private String state;
    private String stateDomicile;
    private String city;
    
    // Status
    private String registrationStatus;
    private String documentVerificationStatus;
    private String choiceLocked;
    private String allotmentStatus;
    
    // Documents
    private Map<String, String> documents;
    
    // Timestamps
    private Date createdDate;
    private Date verifiedDate;
    private String rejectionReason;

    // ==================== CONSTRUCTORS ====================
    
    public Student() {
    }
    
    // Constructor with basic fields (for backward compatibility)
    public Student(String username, String password, String fullName, String email, String phone,
                   Date dateOfBirth, String gender, String category, String neetRollNumber,
                   int neetScore, int neetRank, double neetPercentile, String state,
                   String city, String address, String aadharNumber) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.category = category;
        this.neetRollNumber = neetRollNumber;
        this.neetScore = neetScore;
        this.neetRank = neetRank;
        this.neetPercentile = neetPercentile;
        this.state = state;
        this.city = city;
        this.address = address;
        this.aadharNumber = aadharNumber;
    }

    // ==================== GETTERS AND SETTERS ====================
    
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // ==================== NEW FIELDS - GETTERS & SETTERS ====================

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getNeetRollNumber() {
        return neetRollNumber;
    }

    public void setNeetRollNumber(String neetRollNumber) {
        this.neetRollNumber = neetRollNumber;
    }

    public String getNeetApplicationNumber() {
        return neetApplicationNumber;
    }

    public void setNeetApplicationNumber(String neetApplicationNumber) {
        this.neetApplicationNumber = neetApplicationNumber;
    }

    public int getNeetScore() {
        return neetScore;
    }

    public void setNeetScore(int neetScore) {
        this.neetScore = neetScore;
    }

    public int getNeetRank() {
        return neetRank;
    }

    public void setNeetRank(int neetRank) {
        this.neetRank = neetRank;
    }

    public double getNeetPercentile() {
        return neetPercentile;
    }

    public void setNeetPercentile(double neetPercentile) {
        this.neetPercentile = neetPercentile;
    }

    // Class 10 Details
    public String getClass10School() {
        return class10School;
    }

    public void setClass10School(String class10School) {
        this.class10School = class10School;
    }

    public String getClass10Board() {
        return class10Board;
    }

    public void setClass10Board(String class10Board) {
        this.class10Board = class10Board;
    }

    public String getClass10Year() {
        return class10Year;
    }

    public void setClass10Year(String class10Year) {
        this.class10Year = class10Year;
    }

    public String getClass10Marks() {
        return class10Marks;
    }

    public void setClass10Marks(String class10Marks) {
        this.class10Marks = class10Marks;
    }

    // Class 12 Details
    public String getClass12School() {
        return class12School;
    }

    public void setClass12School(String class12School) {
        this.class12School = class12School;
    }

    public String getClass12Board() {
        return class12Board;
    }

    public void setClass12Board(String class12Board) {
        this.class12Board = class12Board;
    }

    public String getClass12Year() {
        return class12Year;
    }

    public void setClass12Year(String class12Year) {
        this.class12Year = class12Year;
    }

    public String getClass12Marks() {
        return class12Marks;
    }

    public void setClass12Marks(String class12Marks) {
        this.class12Marks = class12Marks;
    }

    public String getPcbMarks() {
        return pcbMarks;
    }

    public void setPcbMarks(String pcbMarks) {
        this.pcbMarks = pcbMarks;
    }

    // Identity Proof
    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getIdentityProofType() {
        return identityProofType;
    }

    public void setIdentityProofType(String identityProofType) {
        this.identityProofType = identityProofType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    // Category & Location
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDomicile() {
        return stateDomicile;
    }

    public void setStateDomicile(String stateDomicile) {
        this.stateDomicile = stateDomicile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // Status Fields
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getDocumentVerificationStatus() {
        return documentVerificationStatus;
    }

    public void setDocumentVerificationStatus(String documentVerificationStatus) {
        this.documentVerificationStatus = documentVerificationStatus;
    }

    public String getChoiceLocked() {
        return choiceLocked;
    }

    public void setChoiceLocked(String choiceLocked) {
        this.choiceLocked = choiceLocked;
    }

    public String getAllotmentStatus() {
        return allotmentStatus;
    }

    public void setAllotmentStatus(String allotmentStatus) {
        this.allotmentStatus = allotmentStatus;
    }

    // Documents
    public Map<String, String> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, String> documents) {
        this.documents = documents;
    }

    // Timestamps
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Date verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    // ==================== UTILITY METHODS ====================
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", email='" + email + '\'' +
                ", neetRollNumber='" + neetRollNumber + '\'' +
                ", neetRank=" + neetRank +
                ", category='" + category + '\'' +
                '}';
    }
 // ==================== CHOICE LOCKED ====================
    private String choiceLocked1 = "NO";  // Add field

    public String getChoiceLocked1() {
        return choiceLocked;
    }

    public void setChoiceLocked1(String choiceLocked) {
        this.choiceLocked = choiceLocked;
    }

    // ==================== ALLOTMENT STATUS ====================
    private String allotmentStatus1 = "NOT_ALLOTTED";  // Add field

    public String getAllotmentStatus1() {
        return allotmentStatus;
    }

    public void setAllotmentStatus1(String allotmentStatus) {
        this.allotmentStatus = allotmentStatus;
    }
    

}
