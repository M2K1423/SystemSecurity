package com.example.webbongden.services;

import com.example.webbongden.dao.InvoiceDao;
import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.ShippingDao;
import com.example.webbongden.dao.model.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class OrderSevices {
    public final OrderDao orderDao;
    public final InvoiceDao invoiceDao;
    public final ShippingDao shippingDao;

    public OrderSevices() {
        this.orderDao = new OrderDao();
        this.invoiceDao = new InvoiceDao();
        this.shippingDao = new ShippingDao();
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

            // ✅ Bước 6: Truy vấn lại Order để lấy ngày tạo thật
            Order order = orderDao.getOrderById(orderId);
            if (order == null) throw new RuntimeException("Không tìm thấy đơn hàng sau khi tạo.");

            // ✅ Bước 7: Tạo rawData (chuỗi cần ký) và hash
            String rawData = generateRawData(order.getId(), order.getCustomerName(), order.getTotalPrice(), order.getCreatedAt());
            byte[] rawDataBytes = rawData.getBytes(StandardCharsets.UTF_8);
            String hashBase64 = Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(rawDataBytes)
            );
            orderDao.updateOrderHash(orderId, hashBase64);
            System.out.println("✅ Mã hash đơn hàng #" + orderId + ": " + hashBase64);
            System.out.println("🔐 rawData = " + rawData);

            // ✅ Bước 8: Ký số
            try {
                URL keystoreURL = getClass().getClassLoader().getResource("keystore.p12");
                if (keystoreURL == null) {
                    throw new RuntimeException("❌ Không tìm thấy file keystore.p12 trong resources/");
                }

                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                try (InputStream is = new FileInputStream(keystoreURL.getPath())) {
                    keyStore.load(is, "keystorePassword".toCharArray()); // sửa mật khẩu nếu khác
                }

                String alias = keyStore.aliases().nextElement();
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, "keystorePassword".toCharArray());
                X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

                byte[] signatureBytes = signData(rawDataBytes, privateKey);
                String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);
                String certBase64 = Base64.getEncoder().encodeToString(cert.getEncoded());

                orderDao.updateDigitalSignature(orderId, signatureBase64, certBase64, hashBase64);
                System.out.println("✅ Đã lưu chữ ký số cho đơn hàng #" + orderId);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("❌ Lỗi khi ký số đơn hàng #" + orderId);
            }

            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Đã xảy ra lỗi trong quá trình tạo hóa đơn và đơn hàng.", e);
        }
    }


    public void updateOrderStatus(int orderId, boolean isSigned) {
        orderDao.updateOrderSignedStatus(orderId, isSigned);
    }
    private byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }


    private String generateRawData(int orderId, String customerName, double total, Date createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String totalStr = String.format("%.2f", total);
        customerName = customerName.trim();
        String rawData = orderId + customerName + totalStr + sdf.format(createdAt);
        System.out.println("Generated Raw Data: " + rawData);
        return rawData;
    }
    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

}
