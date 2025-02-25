package com.example.webbongden.dao.model;

public class SubCategory {
    private int id;
    private int categoryId;
    private String name;
    private int stockQuantity;

    // Constructor không tham số
    public SubCategory() {
    }

    // Constructor đầy đủ tham số
    public SubCategory(int id, int categoryId, String name, int stockQuantity) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Phương thức toString để hiển thị thông tin đối tượng
    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}

