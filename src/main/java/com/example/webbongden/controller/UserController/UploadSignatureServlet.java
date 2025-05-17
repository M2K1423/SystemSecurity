package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
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

@WebServlet("/upload-signature")
@MultipartConfig
public class UploadSignatureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get order ID
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            request.setAttribute("error", "Thiếu mã đơn hàng.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã đơn hàng không hợp lệ.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }

        // Fetch order
        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            request.setAttribute("error", "Không tìm thấy đơn hàng.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }
        request.setAttribute("order", order);

        // Get signature
        String signatureBase64 = null;
        String pasted = request.getParameter("signature");
        if (pasted != null && !pasted.trim().isEmpty()) {
            signatureBase64 = pasted.trim();
        } else {
            Part filePart = request.getPart("signatureFile");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream is = filePart.getInputStream()) {
                    signatureBase64 = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                }
            }
        }

        if (signatureBase64 == null || signatureBase64.isEmpty()) {
            request.setAttribute("error", "Vui lòng dán chữ ký hoặc tải lên file chữ ký.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }

        // Validate Base64
        try {
            Base64.getDecoder().decode(signatureBase64);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Chữ ký Base64 không hợp lệ.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }

        // Check digital certificate
        if (order.getDigitalCert() == null) {
            System.out.println("Order ID: " + orderId + ", DigitalCert is null");
            request.setAttribute("error", "Đơn hàng chưa có chứng thư số để xác minh.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }

        try {
            // Generate raw data
            String rawData = generateRawData(order.getId(), order.getCustomerName(), order.getTotalPrice(), order.getCreatedAt());

            // Compute hash for debugging
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] computedHash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            String computedHashBase64 = Base64.getEncoder().encodeToString(computedHash);

            // Get stored hash for verification
            byte[] hashBytes;
            boolean isValid = false;
            if (order.getHashValue() != null && !order.getHashValue().isEmpty()) {
                try {
                    hashBytes = Base64.getDecoder().decode(order.getHashValue());
                } catch (IllegalArgumentException e) {
                    request.setAttribute("error", "Hash lưu trữ không hợp lệ.");
                    request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
                    return;
                }
            } else {
                hashBytes = computedHash;
                System.out.println("Warning: No stored hash found, using computed hash for verification.");
            }

            // Log for debugging
            System.out.println("Order ID: " + orderId);
            System.out.println("Raw Data: " + rawData);
            System.out.println("Computed Hash: " + computedHashBase64);
            System.out.println("Stored Hash: " + order.getHashValue());
            System.out.println("Certificate Base64: " + order.getDigitalCert());
            System.out.println("Signature Base64: " + signatureBase64);

            // Extract public key
            byte[] certBytes = Base64.getDecoder().decode(order.getDigitalCert());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            cert.checkValidity();
            PublicKey publicKey = cert.getPublicKey();
            System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
            System.out.println("Public Key Format: " + publicKey.getFormat());

            // Verify with stored hash
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            System.out.println("Updating verifier with hash: " + Base64.getEncoder().encodeToString(hashBytes));
            sig.update(rawData.getBytes(StandardCharsets.UTF_8)); // ✅ Dùng rawData gốc

            isValid = sig.verify(signatureBytes);
            System.out.println("Verification Result (Stored Hash): " + isValid);

            // If verification fails, try with computed hash
            if (!isValid) {
                System.out.println("Warning: Verification with stored hash failed. Trying with computed hash.");
                sig.initVerify(publicKey);
                sig.update(rawData.getBytes(StandardCharsets.UTF_8)); // ✅ Dùng rawData gốc

                isValid = sig.verify(signatureBytes);
                System.out.println("Verification Result (Computed Hash): " + isValid);
            }


            request.setAttribute("valid", isValid);
        } catch (CertificateException e) {
            e.printStackTrace();
            request.setAttribute("error", "Chứng thư số không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi xác minh chữ ký: " + e.getMessage());
        }

        request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
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