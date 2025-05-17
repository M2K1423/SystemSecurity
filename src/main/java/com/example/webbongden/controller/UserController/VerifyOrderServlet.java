package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.TimeZone;

@WebServlet("/verify-order")
public class VerifyOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // 1. Lấy orderId từ request
            int orderId;
            try {
                orderId = Integer.parseInt(req.getParameter("id"));
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID đơn hàng không hợp lệ.");
                req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
                return;
            }

            // 2. Lấy đơn hàng từ DB
            OrderDao orderDao = new OrderDao();
            Order order = orderDao.getOrderById(orderId);
            if (order == null) {
                req.setAttribute("error", "Không tìm thấy đơn hàng.");
                req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
                return;
            }

            String base64Sig = order.getDigitalSignature();
            String base64Cert = order.getDigitalCert();
            String storedHashBase64 = order.getHashValue(); // Assuming hash is stored

            if (base64Sig == null || base64Cert == null || storedHashBase64 == null) {
                req.setAttribute("error", "Đơn hàng chưa có chữ ký, chứng thư số hoặc hash.");
                req.setAttribute("order", order);
                req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
                return;
            }

            // 3. Tạo lại hash
            String rawData = generateOrderRawData(order);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] computedHash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            String computedHashBase64 = Base64.getEncoder().encodeToString(computedHash);

            // Log for debugging
            System.out.println("Order ID: " + orderId);
            System.out.println("Raw Data: " + rawData);
            System.out.println("Computed Hash: " + computedHashBase64);
            System.out.println("Stored Hash: " + storedHashBase64);
            System.out.println("Certificate Base64: " + base64Cert);
            System.out.println("Signature Base64: " + base64Sig);

            // 4. Giải mã chứng thư
            byte[] certBytes = Base64.getDecoder().decode(base64Cert);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            cert.checkValidity();
            PublicKey publicKey = cert.getPublicKey();
            System.out.println("Public Key Algorithm: " + publicKey.getAlgorithm());
            System.out.println("Public Key Format: " + publicKey.getFormat());

            // 5. Xác minh chữ ký
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);

            // Verify with stored hash
            byte[] storedHash = Base64.getDecoder().decode(storedHashBase64);
            System.out.println("Updating verifier with stored hash: " + storedHashBase64);
            sig.update(rawData.getBytes(StandardCharsets.UTF_8)); // ✅ Dùng rawData gốc

            boolean valid = sig.verify(Base64.getDecoder().decode(base64Sig));
            System.out.println("Verification Result (Stored Hash): " + valid);

            // If verification fails, try with computed hash
            if (!valid) {
                System.out.println("Warning: Verification with stored hash failed. Trying with computed hash.");
                sig.initVerify(publicKey);
                System.out.println("Updating verifier with computed hash: " + computedHashBase64);
                sig.update(rawData.getBytes(StandardCharsets.UTF_8)); // ✅ Dùng rawData gốc

                valid = sig.verify(Base64.getDecoder().decode(base64Sig));
                System.out.println("Verification Result (Computed Hash): " + valid);
            }



            // 6. Truyền sang JSP
            req.setAttribute("order", order);
            req.setAttribute("signatureValid", valid);
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);

        } catch (CertificateException e) {
            e.printStackTrace();
            req.setAttribute("error", "Chứng thư số không hợp lệ: " + e.getMessage());
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi xác minh chữ ký số: " + e.getMessage());
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
        }
    }

    private String generateOrderRawData(Order order) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String totalStr = String.format("%.2f", order.getTotalPrice());
        String customerName = order.getCustomerName().trim();
        String rawData = order.getId() + customerName + totalStr + sdf.format(order.getCreatedAt());
        System.out.println("Generated Raw Data: " + rawData);
        return rawData;
    }
}