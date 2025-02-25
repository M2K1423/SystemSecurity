package com.example.webbongden.dao.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String categoryName;
    private int stockQuantity;
    private String desc1;
    private List<SubCategory> subCategories = new ArrayList<SubCategory>();

    // Constructor không tham số
    public Category() {
    }

    // Constructor đầy đủ tham số
    public Category(int id, String categoryName, int stockQuantity, String desc1) {
        this.id = id;
        this.categoryName = categoryName;
        this.stockQuantity = stockQuantity;
        this.desc1 = desc1;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
    // Phương thức toString (in ra đối tượng dưới dạng chuỗi)
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", desc1='" + desc1 + '\'' +
                '}';
    }
}

