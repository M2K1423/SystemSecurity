package com.example.webbongden.dao.model;

import java.util.Date;
import java.util.List;

public class User {
    private String customerId; // ID của khách hàng (ví dụ: C001)
    private String customerName; // Tên khách hàng
    private String email; // Email của khách hàng
    private String phone; // Số điện thoại
    private String address; // Địa chỉ
    private Date createdAt; // Ngày đăng ký
    private List<Order> orderHistory; // Danh sách lịch sử mua hàng
    private String publicKey;

    // Constructor tổng hợp
    public User(String customerId, String customerName, String email, String phone, String address, Date createdAt, List<Order> orderHistory) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.orderHistory = orderHistory;
    }

    public User(String customerId, String customerName, String phone, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
    }

    public User(String customerId, String customerName, String phone, String address, String publicKey) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.publicKey = publicKey;
    }

    // Getter và Setter
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getPublicKey() {return publicKey; }

    public void setPublicKey(String publicKey) {this.publicKey = publicKey; }

    // Phương thức toString để hiển thị thông tin
    @Override
    public String toString() {
        return "CustomerAccount{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", orderHistory=" + orderHistory +
                '}';
    }
}

