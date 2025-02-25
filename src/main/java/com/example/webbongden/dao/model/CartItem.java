package com.example.webbongden.dao.model;

import java.text.DecimalFormat;

public class CartItem {
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double price;
    private String imageUrl;
    private String giftName;

    public CartItem() {

    }

    public CartItem(int productId, String productName, int quantity, double unitPrice,double price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }




    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }


    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    // Getter and Setter for productId
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Getter and Setter for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter and Setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter and Setter for price
    public double getPrice() {
        return price;
    }



    public void setPrice(double price) {
        this.price = price;
    }

    // Getter and Setter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getAmount() {
        return this.quantity * this.price;
    }

    public String getFormattedPrice() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price); // Trả về giá tiền đã định dạng, ví dụ: "1.200.000,00 ₫"
    }
}
