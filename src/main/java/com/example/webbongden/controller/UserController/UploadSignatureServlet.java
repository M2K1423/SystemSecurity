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
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@WebServlet("/upload-signature")
@MultipartConfig // Bắt buộc để xử lý file upload
public class UploadSignatureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Part filePart = request.getPart("signatureFile");

        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("message", "❌ Vui lòng tải lên file chữ ký số (.sig)");
            request.getRequestDispatcher("/user/verify_order.jsp").forward(request, response);
            return;
        }

        // Đọc nội dung chữ ký từ file
        String signatureBase64;
        try (InputStream is = filePart.getInputStream()) {
            signatureBase64 = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        }

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);

        if (order == null || order.getDigitalCert() == null) {
            request.setAttribute("message", "❌ Không tìm thấy đơn hàng hoặc chưa có chứng thư số.");
            request.getRequestDispatcher("/user/verify_order.jsp").forward(request, response);
            return;
        }

        try {
            // ✅ 1. Dữ liệu gốc để xác minh
            String rawData = order.getId()
                    + order.getCustomerName()
                    + order.getTotalPrice()
                    + order.getCreatedAt().toString();

            byte[] hashData = MessageDigest.getInstance("SHA-256").digest(rawData.getBytes(StandardCharsets.UTF_8));

            // ✅ 2. Giải mã public key từ cert
            byte[] certBytes = Base64.getDecoder().decode(order.getDigitalCert());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certBytes));
            PublicKey publicKey = cert.getPublicKey();

            // ✅ 3. Xác minh chữ ký
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(hashData);

            boolean isValid = signature.verify(signatureBytes);

            if (isValid) {
                request.setAttribute("message", "✅ Chữ ký hợp lệ. Đơn hàng được xác minh thành công.");
            } else {
                request.setAttribute("message", "❌ Chữ ký không hợp lệ.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "❌ Lỗi xác minh chữ ký: " + e.getMessage());
        }

        request.getRequestDispatcher("/user/verify_order.jsp").forward(request, response);
    }
}
