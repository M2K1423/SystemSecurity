package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;

@WebServlet("/verify-order")
public class VerifyOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            // 1. Lấy orderId từ request
            int orderId = Integer.parseInt(req.getParameter("id"));

            // 2. Lấy đơn hàng từ DB
            OrderDao orderDao = new OrderDao();
            Order order = orderDao.getOrderById(orderId); // đảm bảo đã có hàm này

            if (order == null) {
                req.setAttribute("error", "Không tìm thấy đơn hàng.");
                req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
                return;
            }

            String base64Sig = order.getDigitalSignature();
            String base64Cert = order.getDigitalCert();

            if (base64Sig == null || base64Cert == null) {
                req.setAttribute("error", "Đơn hàng chưa có chữ ký hoặc chứng thư số.");
                req.setAttribute("order", order);
                req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
                return;
            }

            // 3. Tạo lại hash
            String hash = generateOrderHash(order);

            // 4. Giải mã chứng thư
            byte[] certBytes = Base64.getDecoder().decode(base64Cert);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));

            // 5. Xác minh chữ ký
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(cert.getPublicKey());
            sig.update(hash.getBytes());

            boolean valid = sig.verify(Base64.getDecoder().decode(base64Sig));

            // 6. Truyền sang JSP
            req.setAttribute("order", order);
            req.setAttribute("signatureValid", valid);
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID đơn hàng không hợp lệ.");
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi xác minh chữ ký số: " + e.getMessage());
            req.getRequestDispatcher("/user/verify_order.jsp").forward(req, resp);
        }
    }

    private String generateOrderHash(Order order) throws NoSuchAlgorithmException {
        String data = order.getId()
                + order.getCustomerName()
                + order.getTotalPrice()
                + new SimpleDateFormat("dd-MM-yyyy").format(order.getCreatedAt()); // đồng bộ format với lúc ký

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest(data.getBytes()));
    }
}
