package com.example.webbongden.dao.model;

import java.util.Date;

public class Customer {
    private int id; // ID của khách hàng
    private String cusName; // Tên khách hàng
    private String idNumber; // Số CMND/CCCD
    private String sex; // Giới tính
    private Date birthday; // Ngày sinh
    private String address; // Địa chỉ
    private String phone; // Số điện thoại
    private String note;

    // Constructor không tham số
    public Customer(int id, String cusName, String address, String phone, String note) {
        this.id = id;
        this.cusName = cusName;
        this.address = address;
        this.phone = phone;
        this.note = note;
    }

    public Customer() {

    }

    // Constructor đầy đủ tham số
    public Customer(int id, String cusName, String idNumber, String sex, Date birthday, String address, String phone) {
        this.id = id;
        this.cusName = cusName;
        this.idNumber = idNumber;
        this.sex = sex;
        this.birthday = birthday;
        this.address = address;
        this.phone = phone;
    }

    // Getter và Setter cho từng thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Phương thức toString để hiển thị thông tin khách hàng
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", cusName='" + cusName + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

