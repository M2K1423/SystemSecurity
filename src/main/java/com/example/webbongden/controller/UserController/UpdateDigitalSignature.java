package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import jakarta.servlet.annotation.MultipartConfig;

@WebServlet(name = "UpdateDigitalSignature", value = "/update-digital-signature")
@MultipartConfig
public class UpdateDigitalSignature extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set kiểu trả về
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // lấy mã đơn hàng và chữ kí
        String orderIdParam = request.getParameter("orderId");
        String digitalSignature = request.getParameter("digitalSignature");

        String json;

        try {
            // kiểm tra mã đơn hàng và chữ kí
            if (orderIdParam == null || orderIdParam.trim().isEmpty() || digitalSignature == null || digitalSignature.trim().isEmpty()) {
                json = "{\"success\": false, \"message\": \"Thiếu dữ liệu đầu vào.\"}";
            } else {
                // đổi chữ kí từ string sang int
                int orderId = Integer.parseInt(orderIdParam);

                // gọi orderDao để cập nhật chữ kí
                int result = orderDao.updateDigitalSignature(orderId, digitalSignature);

                // set nội dung trả về thành công nếu số dòng trong database bị ảnh hưởng > 0, nếu ngược lại trả về thất bại
                if (result > 0) {
                    json = "{\"success\": true, \"message\": \"Cập nhật chữ ký cho đơn hàng "+ orderIdParam + " thành công.\"}";
                } else {
                    json = "{\"success\": false, \"message\": \"Không thể cập nhật chữ ký cho đơn hàng " + orderIdParam + ".\"}";
                }
            }
        } catch (Exception e) {
            // xử lý ngoại lệ
            json = "{\"success\": false, \"message\": \"Lỗi server: " + e.getMessage() + "\"}";
        }
        // trả về cho client
        response.getWriter().write(json);
    }

}