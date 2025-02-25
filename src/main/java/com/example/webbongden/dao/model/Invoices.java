package com.example.webbongden.dao.model;

import java.util.Date;

public class Invoices {
    private int id; // ID của hóa đơn
    private int promotionId; // ID khuyến mãi
    private int accountId; // ID tài khoản
    private Date createdAt; // Ngày tạo hóa đơn
    private double totalPrice; // Tổng giá trị hóa đơn
    private String paymentStatus; // Trạng thái thanh toán (Paid, Unpaid, etc.)

    // Constructor mặc định
    public Invoices() {}

    // Constructor đầy đủ
    public Invoices(int id, int promotionId, int accountId, Date createdAt, double totalPrice, String paymentStatus) {
        this.id = id;
        this.promotionId = promotionId;
        this.accountId = accountId;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Invoices{" +
                "id=" + id +
                ", promotionId=" + promotionId +
                ", accountId=" + accountId +
                ", createdAt=" + createdAt +
                ", totalPrice=" + totalPrice +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
