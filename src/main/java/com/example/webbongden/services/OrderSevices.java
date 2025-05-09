package com.example.webbongden.services;

import com.example.webbongden.dao.InvoiceDao;
import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.ShippingDao;
import com.example.webbongden.dao.model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class OrderSevices {
    public final OrderDao orderDao;
    public final InvoiceDao invoiceDao;
    public final ShippingDao shippingDao;

    public OrderSevices() {
        this.orderDao = new OrderDao();
        this.invoiceDao = new InvoiceDao();
        this.shippingDao = new ShippingDao();
    }
    private String generateOrderHash(int orderId, String customerName, double total, Date date) throws NoSuchAlgorithmException {
        String orderData = orderId + customerName + total + date.toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest(orderData.getBytes()));
    }

    private byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }


    public int getTotalOrders() {
        return orderDao.totalOrderInLastedMonth();
    }

    public int getPendingOrders() {
        return orderDao.totalPendingOrders();
    }

    public int getShippingOrders() {
        return orderDao.totalShippingOrders();
    }

    public List<Order> getAllOrders() {
        return orderDao.getListOrders();
    }

    public List<Order> getOrdersInLastMonth() {
        return orderDao.getOrdersInLastMonth();
    }

    public List<Order> getOrdersByKeyWord(String value) {
        return orderDao.getOrdersByKeyword(value);
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDao.updateOrderStatus(orderId, status);
    }

    public List<Order> filterOrderByStatus(String value) {
        return orderDao.filterOrderByStatus(value);
    }

    public double getTotalRevenue() {
        return orderDao.getTotalRevenue();
    }

    public List<OrderDetail> getOrderDetailsById(int orderId) {
        return orderDao.getOrderDetailsByOrderId(orderId);
    }

    //User
    public List<Order> getOrdersByUsername(String username) {
        return orderDao.getOrdersByUsername(username);
    }

    public int createOrderAndInvoice(Invoices invoice, List<OrderDetail> orderDetails, Customer customerInfo) {
        try {
            // Bước 1: Tạo hóa đơn
            int invoiceId = invoiceDao.createInvoice(invoice);
            invoice.setId(invoiceId);

            // Bước 2: Tạo chi tiết hóa đơn
            invoiceDao.createInvoiceDetails(invoiceId, orderDetails);

            // Bước 3: Tạo đơn hàng từ hóa đơn
            int orderId = orderDao.createOrderFromInvoice(invoice, customerInfo);

            // ✅ Bước 3.1: Sinh mã hash để test
            String hash = generateOrderHash(orderId, customerInfo.getCusName(), invoice.getTotalPrice(), invoice.getCreatedAt());
            System.out.println("✅ Mã hash đơn hàng #" + orderId + ": " + hash);

            // Bước 4: Tạo chi tiết đơn hàng
            orderDao.createOrderDetails(orderId, orderDetails);

            // Bước 5: Tạo thông tin vận chuyển
            Shipping shipping = new Shipping();
            shipping.setOrderId(orderId);
            shipping.setPickupDate(new Date());
            shipping.setShippingStatus("Pending");
            shipping.setAddress(customerInfo.getAddress());
            shipping.setCarrier("J&T Express");

            shippingDao.insertShipping(shipping);

            // ✅ Return lại mã đơn hàng để controller xử lý tiếp
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Đã xảy ra lỗi trong quá trình tạo hóa đơn và đơn hàng.", e);
        }
    }

    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

}
