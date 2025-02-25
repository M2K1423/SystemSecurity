package com.example.webbongden.dao.model;

public class TopProduct {
    private String productName;
    private int quantitySold;
    private double totalRevenue;
    private int stockQuantity;

    public TopProduct(String productName, int quantitySold, double totalRevenue, int stockQuantity) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
        this.stockQuantity = stockQuantity;
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    // Định dạng doanh thu thành chuỗi tiền tệ
    public String getFormattedRevenue() {
        return String.format("%,.0f VND", totalRevenue);
    }
}
