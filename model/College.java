package com.neet.model;

import java.util.Date;

/**
 * College Model Class
 */
public class College {
    private int collegeId;
    private String collegeCode;
    private String collegeName;
    private String username;
    private String password;
    private String collegeType;
    private String state;
    private String city;
    private String email;
    private String phone;
    private int totalSeats;
    private int availableSeats;
    private String status;
    private Date createdDate;
    
    // Constructors
    public College() {
    }
    
    public College(String collegeCode, String collegeName, String username, String password,
                   String collegeType, String state, String city, String email, String phone,
                   int totalSeats) {
        this.collegeCode = collegeCode;
        this.collegeName = collegeName;
        this.username = username;
        this.password = password;
        this.collegeType = collegeType;
        this.state = state;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.status = "ACTIVE";
    }
    
    // Getters and Setters
    public int getCollegeId() { return collegeId; }
    public void setCollegeId(int collegeId) { this.collegeId = collegeId; }
    
    public String getCollegeCode() { return collegeCode; }
    public void setCollegeCode(String collegeCode) { this.collegeCode = collegeCode; }
    
    public String getCollegeName() { return collegeName; }
    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getCollegeType() { return collegeType; }
    public void setCollegeType(String collegeType) { this.collegeType = collegeType; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    
    @Override
    public String toString() {
        return "College{" +
                "collegeId=" + collegeId +
                ", collegeName='" + collegeName + '\'' +
                ", collegeType='" + collegeType + '\'' +
                ", state='" + state + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
