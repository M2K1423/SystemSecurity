package com.example.webbongden.dao.model;

import java.util.ArrayList;
import java.util.Date;

public class Promotion {
    private int id; // ID khuyến mãi
    private int productId; // ID sản phẩm được áp dụng khuyến mãi
    private String promotionName; // Tên khuyến mãi
    private Date startDay; // Ngày bắt đầu khuyến mãi
    private Date endDay; // Ngày kết thúc khuyến mãi
    private double discountPercent; // Phần trăm giảm giá
    private String promotionType; // Loại khuyến mãi (ví dụ: "Quà tặng", "Giảm giá")
    private ArrayList<Product> products;

    // Constructor mặc định
    public Promotion() {
    }
    public Promotion(int id, String promotionName, Date startDay, Date endDay, double discountPercent, String promotionType, ArrayList<Product> products) {
        this.id = id;
        this.promotionName = promotionName;
        this.startDay = startDay;
        this.endDay = endDay;
        this.discountPercent = discountPercent;
        this.promotionType = promotionType;
        this.products = products;
    }

    // Constructor đầy đủ
    public Promotion(int id, int productId, String promotionName, Date startDay, Date endDay, double discountPercent, String promotionType) {
        this.id = id;
        this.productId = productId;
        this.promotionName = promotionName;
        this.startDay = startDay;
        this.endDay = endDay;
        this.discountPercent = discountPercent;
        this.promotionType = promotionType;
    }


    public Promotion(String promotionName, Date endDay, double discountPercent, String promotionType) {
        this.promotionName = promotionName;
        this.endDay = endDay;
        this.discountPercent = discountPercent;
        this.promotionType = promotionType;
    }

    // Getter và Setter


    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    // Override phương thức toString để hiển thị thông tin
    @Override
    public String toString() {
        return "Promotions{" +
                "id=" + id +
                ", productId=" + productId +
                ", promotionName='" + promotionName + '\'' +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", discountPercent=" + discountPercent +
                ", promotionType='" + promotionType + '\'' +
                '}';
    }
}
