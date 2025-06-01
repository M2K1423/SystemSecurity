package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/verify-signature")
@MultipartConfig
public class VerifySignatureServlet extends HttpServlet {
    private static final OrderSevices orderSevices;

    static {
        orderSevices = new OrderSevices();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Lấy orderId
        int orderId;
        try {
            orderId = Integer.parseInt(req.getParameter("orderId"));
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Mã đơn hàng không hợp lệ.");
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
            return;
        }

        // Lấy chữ ký người dùng
        String signatureBase64 = null;
        String pasted = req.getParameter("signature");
        if (pasted != null && !pasted.trim().isEmpty()) {
            signatureBase64 = pasted.trim();
        } else {
            Part filePart = req.getPart("signatureFile");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream is = filePart.getInputStream()) {
                    signatureBase64 = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                }
            }
        }

        if (signatureBase64 == null || signatureBase64.isEmpty()) {
            req.setAttribute("error", "Thiếu chữ ký người dùng.");
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
            return;
        }

        // Validate Base64
        try {
            Base64.getDecoder().decode(signatureBase64);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Chữ ký Base64 không hợp lệ.");
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
            return;
        }

        // Lấy đơn hàng
        Order order = orderSevices.getOrderById(orderId);
        if (order == null || order.getDigitalCert() == null || order.getHashValue() == null) {
            req.setAttribute("error", "Không tìm thấy dữ liệu, thiếu chứng thư số hoặc hash.");
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
            return;
        }

        try {
            // Tạo rawData giống lúc ký
            String rawData = generateRawData(order.getId(), order.getCustomerName(), order.getTotalPrice(), order.getCreatedAt());

            // Compute hash of rawData
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] computedHash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            String computedHashBase64 = Base64.getEncoder().encodeToString(computedHash);

            // Log for debugging
            System.out.println("Order ID: " + orderId);
            System.out.println("Raw Data: " + rawData);
            System.out.println("Computed Hash: " + computedHashBase64);
            System.out.println("Stored Hash: " + order.getHashValue());
            System.out.println("Certificate Base64: " + order.getDigitalCert());
            System.out.println("Signature Base64: " + signatureBase64);

            // Giải mã certificate -> public key
            byte[] certBytes = Base64.getDecoder().decode(order.getDigitalCert());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            cert.checkValidity();
            PublicKey publicKey = cert.getPublicKey();
            System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
            System.out.println("Public Key Format: " + publicKey.getFormat());

            // Giải mã chữ ký và xác minh
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);

            // Verify with computed hash
            System.out.println("Updating verifier with computed hash: " + computedHashBase64);
            verifier.update(rawData.getBytes(StandardCharsets.UTF_8));

            boolean isValid = verifier.verify(signatureBytes);
            System.out.println("Verification Result (Computed Hash): " + isValid);

            if (isValid) {
                order = orderSevices.getOrderById(order.getId());

                orderSevices.updateOrderStatus(order.getId(), true);

            }

            if (!isValid) {
                String storedHashBase64 = order.getHashValue();
                if (storedHashBase64 == null || storedHashBase64.isEmpty()) {
                    System.out.println("No stored hash available for fallback verification.");
                    return;
                }

                byte[] storedHash = Base64.getDecoder().decode(storedHashBase64);
                System.out.println("Warning: Verification with computed hash failed. Trying with stored hash.");

                // Reset lại đối tượng Signature
                verifier = Signature.getInstance("SHA256withRSA"); // Tạo mới để tránh lỗi trạng thái cũ
                verifier.initVerify(publicKey);
                verifier.update(storedHash); // Dùng hash đã lưu

                isValid = verifier.verify(signatureBytes);
                System.out.println("Verification Result (Stored Hash): " + isValid);
            }

            // Truyền kết quả về JSP
            req.setAttribute("order", order);
            req.setAttribute("valid", isValid);
            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);

        } catch (CertificateException e) {
            e.printStackTrace();
            req.setAttribute("error", "Chứng thư số không hợp lệ: " + e.getMessage());
            req.setAttribute("order", order);
            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi xác minh: " + e.getMessage());
            req.setAttribute("order", order);
            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);
        }
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
}