package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@WebServlet("/upload-signature")
public class UploadSignatureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String signatureBase64 = request.getParameter("signature");

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);

        if (order == null || order.getDigitalCert() == null) {
            request.setAttribute("message", "Không tìm thấy đơn hàng hoặc chứng thư số.");
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
            return;
        }

        try {
            // Tạo lại hash từ đơn hàng
            String data = order.getId() + order.getCustomerName() + order.getTotalPrice() + order.getCreatedAt().toString();
            byte[] hashData = java.security.MessageDigest.getInstance("SHA-256").digest(data.getBytes());

            // Lấy chứng thư từ đơn hàng
            byte[] certBytes = Base64.getDecoder().decode(order.getDigitalCert());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));

            // Xác minh chữ ký
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(cert.getPublicKey());
            sig.update(hashData);

            boolean isValid = sig.verify(Base64.getDecoder().decode(signatureBase64));

            request.setAttribute("signatureValid", isValid);
            request.setAttribute("order", order);
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi xác minh chữ ký: " + e.getMessage());
            request.getRequestDispatcher("/user/verify_result.jsp").forward(request, response);
        }
    }
}
