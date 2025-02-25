package com.example.webbongden.dao.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Product {
    private int id;
    private String productName;
    private double unitPrice; // Giá gốc
    private double discountPercent; // Phần trăm giảm giá
    private List<ProductImage> listImg; // Danh sách hình ảnh
    private String categoryName; // Loại sản phẩm
    private Date createdAt; // Ngày thêm sản phẩm
    private int sales;
    //Product dùng cho Home

    public Product() {}
    public Product(int id, String productName, double unitPrice, double discountPercent,
                   List<ProductImage> listImg) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.discountPercent = discountPercent;
        this.listImg = listImg;
    }

    public Product(int id, String imageUrl, String productName, double unitPrice, String categoryName, java.sql.Date createdAt) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.categoryName = categoryName;
        this.createdAt = createdAt;

        // Khởi tạo listImg với một hình ảnh đầu tiên từ URL
        this.listImg = List.of(new ProductImage(imageUrl, true));
    }

    public Product(int id, String imageUrl, String productName, double unitPrice, String categoryName, java.sql.Date createdAt, double discountPercent) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.discountPercent = discountPercent;
        // Khởi tạo listImg với một hình ảnh đầu tiên từ URL
        this.listImg = List.of(new ProductImage(imageUrl, true));
    }

    public Product(int id, String productName, double unitPrice) {
        this.id = id;
        this.productName = productName;
        this.unitPrice = unitPrice;
    }



    // Tính giá sau khi giảm
    public double getDiscountedPrice() {
        return unitPrice - (unitPrice * discountPercent / 100);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public List<ProductImage> getListImg() {
        return listImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    // Lấy URL hình ảnh chính hoặc hình đầu tiên
    public String getImageUrl() {
        if (listImg == null || listImg.isEmpty()) {
            return "default-image.jpg"; // Hình ảnh mặc định nếu không có hình
        }
        for (ProductImage img : listImg) {
            if (img.isMainImage()) {
                return img.getUrl();
            }
        }
        return listImg.get(0).getUrl();
    }
    // Getter và Setter cho sales
    public int getSales() {
        return sales;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", unitPrice=" + unitPrice +
                ", discountPercent=" + discountPercent +
                ", listImg=" + listImg +
                '}';
    }
    public void setSales(int sales) {
        this.sales = sales;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}