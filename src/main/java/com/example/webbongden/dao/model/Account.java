package com.example.webbongden.dao.model;

import java.util.Date;

public class Account {
    private int id; // ID của tài khoản
    private int customerId; // ID của khách hàng liên kết
    private String email; // Email tài khoản
    private String cusName; // Tên khách hàng
    private String username; // Tên đăng nhập
    private String password; // Mật khẩu
    private Date createdAt; // Ngày tạo tài khoản
    private String role; // Vai trò của tài khoản
    private String rePassword;

    // Constructor không tham số
    public Account() {
    }

    // Constructor đầy đủ tham số
    public Account(
            int id,
            int customerId,
            String cusName, // Tên khách hàng
            String email,   // Email
            String username,
            String password,
            Date createdAt,
            String role
    ) {
        this.id = id;
        this.customerId = customerId;
        this.cusName = cusName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }


    public Account(int id, String username, String email, java.util.Date createdAt, String role) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.username = username;
        this.role = role;
    }

    public Account(int id, String username, String email, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public Account(String email, String cusName, String username, String password, String role, int customerId) {
        this.email = email;
        this.cusName = cusName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    // Getter và Setter cho từng thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Phương thức toString để hiển thị thông tin tài khoản
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", email='" + email + '\'' +
                ", cusName='" + cusName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", role='" + role + '\'' +
                '}';
    }
}

