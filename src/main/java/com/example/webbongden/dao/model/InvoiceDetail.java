package com.example.webbongden.dao.model;

public class InvoiceDetail {
    private int invoiceId;      // ID hóa đơn
    private int productId;      // ID sản phẩm
    private double unitPrice;   // Giá mỗi đơn vị sản phẩm
    private int quantity;       // Số lượng sản phẩm
    private double itemDiscount; // Giảm giá áp dụng trên sản phẩm
    private double amount;      // Tổng số tiền (sau giảm giá)

    // Constructor không tham số
    public InvoiceDetail() {
    }

    // Constructor đầy đủ tham số
    public InvoiceDetail(int invoiceId, int productId, double unitPrice, int quantity, double itemDiscount, double amount) {
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.itemDiscount = itemDiscount;
        this.amount = amount;
    }

    // Getter và Setter
    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(double itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Override phương thức toString để hiển thị thông tin chi tiết hóa đơn
    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "invoiceId=" + invoiceId +
                ", productId=" + productId +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", itemDiscount=" + itemDiscount +
                ", amount=" + amount +
                '}';
    }
}
