package com.example.webbongden.dao.model;

import java.util.Date;

public class Shipping {
    private int id;
    private int orderId;
    private Date pickupDate;
    private String shippingStatus;
    private String address;
    private String carrier;

    // Constructor không tham số
    public Shipping() {
    }

    // Constructor có tham số
    public Shipping(int id, int orderId, Date pickupDate, String shippingStatus, String address, String carrier) {
        this.id = id;
        this.orderId = orderId;
        this.pickupDate = pickupDate;
        this.shippingStatus = shippingStatus;
        this.address = address;
        this.carrier = carrier;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    // Phương thức toString
    @Override
    public String toString() {
        return "Shipping{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", pickupDate=" + pickupDate +
                ", shippingStatus='" + shippingStatus + '\'' +
                ", address='" + address + '\'' +
                ", carrier='" + carrier + '\'' +
                '}';
    }
}
