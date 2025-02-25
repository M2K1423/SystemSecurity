package com.example.webbongden.dao.model;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductDetail {
    private int id;
    private int subCategoryId;
    private String productName;
    private double unitPrice;
    private Date createdAt;
    private int stockQuantity;
    private String productStatus;
    private double rating;
    private String description; // desc_1
    private String warrantyPeriod;
    private String lightColor;
    private String material;
    private String voltage;
    private String usageAge;
    private double discountPercent;
    private List<ProductImage> listImages; // Danh sách hình ảnh sản phẩm
    private String categoryName; // Tên danh mục
    private String mainImageUrl;

    // Constructor đầy đủ
    public ProductDetail(int id, int subCategoryId, String productName, double unitPrice, Date createdAt,
                         int stockQuantity, String productStatus, double rating, String description,
                         String warrantyPeriod, String lightColor, String material, String voltage,
                         String usageAge, double discountPercent, List<ProductImage> listImages,
                         String categoryName, String mainImageUrl) {
        this.id = id;
        this.subCategoryId = subCategoryId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
        this.stockQuantity = stockQuantity;
        this.productStatus = productStatus;
        this.rating = rating;
        this.description = description;
        this.warrantyPeriod = warrantyPeriod;
        this.lightColor = lightColor;
        this.material = material;
        this.voltage = voltage;
        this.usageAge = usageAge;
        this.discountPercent = discountPercent;
        this.listImages = listImages;
        this.categoryName = categoryName;
        this.mainImageUrl = mainImageUrl;
    }

    public ProductDetail() {

    }

    // Getters và Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getSubCategoryId() { return subCategoryId; }

    public void setSubCategoryId(int subCategoryId) { this.subCategoryId = subCategoryId; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public double getUnitPrice() { return unitPrice; }

    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public int getStockQuantity() { return stockQuantity; }

    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getProductStatus() { return productStatus; }

    public void setProductStatus(String productStatus) { this.productStatus = productStatus; }

    public double getRating() { return rating; }

    public void setRating(double rating) { this.rating = rating; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getWarrantyPeriod() { return warrantyPeriod; }

    public void setWarrantyPeriod(String warrantyPeriod) { this.warrantyPeriod = warrantyPeriod; }

    public String getLightColor() { return lightColor; }

    public void setLightColor(String lightColor) { this.lightColor = lightColor; }

    public String getMaterial() { return material; }

    public void setMaterial(String material) { this.material = material; }

    public String getVoltage() { return voltage; }

    public void setVoltage(String voltage) { this.voltage = voltage; }

    public String getUsageAge() { return usageAge; }

    public void setUsageAge(String usageAge) { this.usageAge = usageAge; }

    public double getDiscountPercent() { return discountPercent; }

    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public List<ProductImage> getListImages() { return listImages; }

    public void setListImages(List<ProductImage> listImages) { this.listImages = listImages; }

    // Tính giá sau khi giảm giá
    public double getDiscountedPrice() {
        return unitPrice - (unitPrice * discountPercent / 100);
    }

    // Lấy hình ảnh chính (main image) hoặc hình đầu tiên nếu không có
    public String getMainImage() {
        if (listImages == null || listImages.isEmpty()) {
            return "default-image.jpg"; // Trả về hình mặc định nếu không có hình nào
        }
        for (ProductImage img : listImages) {
            if (img.isMainImage()) {
                return img.getUrl();
            }
        }
        return listImages.get(0).getUrl(); // Trả về hình đầu tiên nếu không có main image
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "id=" + id +
                ", subCategoryId=" + subCategoryId +
                ", productName='" + productName + '\'' +
                ", unitPrice=" + unitPrice +
                ", createdAt=" + createdAt +
                ", stockQuantity=" + stockQuantity +
                ", productStatus='" + productStatus + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", warrantyPeriod='" + warrantyPeriod + '\'' +
                ", lightColor='" + lightColor + '\'' +
                ", material='" + material + '\'' +
                ", voltage='" + voltage + '\'' +
                ", usageAge='" + usageAge + '\'' +
                ", discountPercent=" + discountPercent +
                ", listImages=" + listImages +
                ", categoryName='" + categoryName + '\'' +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                '}';
    }

    public String getFormattedDiscountedPrice() {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(unitPrice - (unitPrice * discountPercent / 100)); // Ví dụ: "1.140.000"
    }
}


