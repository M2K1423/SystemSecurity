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
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@WebServlet("/verify-signature")
public class VerifySignatureServlet extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String userSignatureBase64 = req.getParameter("signature");

        Order order = orderDao.getOrderById(orderId);
        if (order == null || order.getDigitalCert() == null || order.getHashValue() == null) {
            req.setAttribute("error", "Không tìm thấy dữ liệu xác thực.");
            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);
            return;
        }

        try {
            // 1. Convert dữ liệu
            byte[] certBytes = Base64.getDecoder().decode(order.getDigitalCert());
            byte[] signatureBytes = Base64.getDecoder().decode(userSignatureBase64);
            byte[] hashBytes = Base64.getDecoder().decode(order.getHashValue());

            // 2. Tạo public key từ certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            PublicKey publicKey = cert.getPublicKey();

            // 3. Xác minh
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(hashBytes);

            boolean isValid = verifier.verify(signatureBytes);

            req.setAttribute("order", order);
            req.setAttribute("isValid", isValid);
            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi xác thực: " + e.getMessage());
            req.getRequestDispatcher("/user/verify_result.jsp").forward(req, resp);
        }
    }
}
