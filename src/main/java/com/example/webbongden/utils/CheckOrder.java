package com.example.webbongden.utils;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.OrderDetail;
import com.example.webbongden.services.PublicKeyServices;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.TimeZone;

public class CheckOrder {

    public static String getPublickey(Order order) {
        PublicKeyServices publicKeyServices = new PublicKeyServices();
        int orderId = order.getId();
        String pubKey = publicKeyServices.getPublicKeyString(orderId);
        if (pubKey == null) {
            throw new RuntimeException("Không tìm thấy public key cho tài khoản ID: " + orderId);
        }
        return pubKey;
    }

    public static PublicKey decodePublicKey(String base64PublicKey) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private static String generateRawData(Order order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Initialize OrderDao to fetch order details
        OrderDao orderDao = new OrderDao();
        List<OrderDetail> orderDetails = orderDao.getOrderDetailsByOrderId(order.getId());

        // Build product list string
        StringBuilder productList = new StringBuilder();
        for (OrderDetail detail : orderDetails) {
            productList.append(detail.getProductId()).append("\r\n")
                    .append(detail.getProductName()).append("\r\n")
                    .append(detail.getQuantity()).append("\r\n")
                    .append(detail.getUnitPrice()).append("\r\n")
                    .append(detail.getAmount()).append("\r\n");
        }

        if (productList.toString().endsWith("\r\n")) {
            productList.delete(productList.length() - 2, productList.length());
        }

        // Construct raw data string
        return order.getCustomerName().trim() + "\r\n" +
                order.getPhone() + "\r\n" +
                order.getId() + "\r\n" +
                order.getCreatedAt() + "\r\n" +
                order.getAddress() + "\r\n" +
                order.getShippingMethod() + "\r\n" +
                order.getShippingFee() + "\r\n" +
                order.getTotalPrice() + "\r\n" +
                productList;
    }

    public static byte[] generateDataByBytes(Order order) {
        return generateRawData(order).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] getSignedBytes(Order order) throws Exception {
        String signature = order.getDigitalSignature();
        return Base64.getDecoder().decode(signature);
    }

    public static boolean checkOrder(Order order) throws Exception {
        // Initialize the Signature object for verification
        Signature verifier = Signature.getInstance("SHA256withRSA");
        // Initialize with the public key
        verifier.initVerify(decodePublicKey(getPublickey(order)));
        // Update with the data to verify (same data used during signing)
        verifier.update(generateDataByBytes(order));
        // Verify the signature
        return verifier.verify(getSignedBytes(order));
    }
}