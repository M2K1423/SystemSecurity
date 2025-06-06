package com.example.webbongden.dao.model;

import com.example.webbongden.dao.OrderDao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;                  // ID của đơn hàng
    private String customerName;     // Tên khách hàng
    private Date createdAt;          // Ngày đặt hàng
    private double totalPrice;   // Tổng tiền của đơn hàng
    private String address;          // Địa chỉ giao hàng
    private String orderStatus;      // Trạng thái đơn hàng
    private List<OrderDetail> orderDetails; // Danh sách chi tiết đơn hàng (nếu cần)
    private double shippingFee;
    private String shippingMethod;
    private int accountId;
    private String note;
    private String digitalSignature;
    private String digitalCert;
    private String hashValue;
    private boolean isSigned;
    private boolean valid; // Biến để kiểm tra tính hợp lệ của chữ ký
    private String phone;
    private int pkId;

    public String getPhone() {return this.phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public int getPkId() {return this.pkId;}

    public void setPkId(int pkId) {this.pkId = pkId;}

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    // Constructors
    public Order() {
    }

    public Order(int id, String customerName, Date createdAt, double totalPrice,
                 String address, String orderStatus, List<OrderDetail> orderDetails,
                 String digitalSignature, String digitalCert) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
        this.digitalSignature = digitalSignature;
        this.digitalCert = digitalCert;
    }
    public Order(int id, String customerName, Date createdAt, double totalPrice,
                 String address, String orderStatus, List<OrderDetail> orderDetails,
                 double shippingFee, String shippingMethod, int accountId, String note,
                 String digitalSignature, String digitalCert, String hashValue, boolean isSigned) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
        this.shippingFee = shippingFee;
        this.shippingMethod = shippingMethod;
        this.accountId = accountId;
        this.note = note;
        this.digitalSignature = digitalSignature;
        this.digitalCert = digitalCert;
        this.hashValue = hashValue;
        this.isSigned = isSigned;
    }

    public Order(int id, String customerName, java.util.Date createdAt, String orderStatus) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }
    public Order(int id, String customerName, Date createdAt, double totalPrice,
                 String address, String orderStatus, boolean isSigned) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.isSigned = isSigned;
    }

    public Order(int id, String customerName, java.util.Date createdAt, double totalPrice,
                 String address, String orderStatus, List<OrderDetail> orderDetails) {
        this.id = id;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.address = address;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }


    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getNote() {
        return note;
    }


    public void setNote(String shippingMethod) {
        this.note = shippingMethod;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getFormattedCreateAt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(this.createdAt);
    }


    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getDigitalCert() {
        return digitalCert;
    }

    public void setDigitalCert(String digitalCert) {
        this.digitalCert = digitalCert;
    }


    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean isSigned) {
        this.isSigned = isSigned;
    }
    public static void main(String[] args) {
        OrderDao orderDao = new OrderDao();

        // Lấy danh sách đơn hàng
        List<Order> orders = orderDao.getListOrders();

        // Duyệt và in giá trị isSigned
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId() + ", isSigned: " + order.isSigned());

            // Kiểm tra trực tiếp boolean isSigned
            if (order.isSigned()) {
                System.out.println("Đơn hàng " + order.getId() + " đã được ký.");
            } else {
                System.out.println("Đơn hàng " + order.getId() + " chưa được ký.");
            }
        }
    }

}