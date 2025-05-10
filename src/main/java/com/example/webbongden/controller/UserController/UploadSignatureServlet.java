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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;

@WebServlet("/upload-signature")
public class UploadSignatureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String signatureBase64 = req.getParameter("signature");

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);

        if (order == null) {
            resp.getWriter().write("Không tìm thấy đơn hàng.");
            return;
        }

        try {
            // 1. Tạo lại dữ liệu hóa đơn
            String hash = generateOrderHash(order);

            // 2. Lấy public key từ chứng thư đã cấp (có thể lưu trước đó)
            byte[] certBytes = Base64.getDecoder().decode(order.getDigitalCert()); // đã lưu lúc ký
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));

            // 3. Xác minh chữ ký
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(cert.getPublicKey());
            sig.update(hash.getBytes());

            boolean valid = sig.verify(Base64.getDecoder().decode(signatureBase64));
            req.setAttribute("verifyResult", valid);
            req.setAttribute("order", order);

            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("Lỗi xác minh: " + e.getMessage());
        }
    }

    private String generateOrderHash(Order order) throws NoSuchAlgorithmException {
        String data = order.getId() + order.getCustomerName() + order.getTotalPrice()
                + new SimpleDateFormat("dd-MM-yyyy").format(order.getCreatedAt());
        return Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-256").digest(data.getBytes()));
    }
}

