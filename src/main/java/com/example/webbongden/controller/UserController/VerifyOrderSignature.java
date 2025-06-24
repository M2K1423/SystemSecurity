package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.utils.CheckOrder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VerifyOrderSignature", value = "/verify-order-signature")
public class VerifyOrderSignature extends HttpServlet {
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String orderIdParam = request.getParameter("orderId");
        String json;

        try {
            if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
                json = "{\"success\": false, \"message\": \"Thiếu mã đơn hàng.\"}";
            } else {
                int orderId = Integer.parseInt(orderIdParam);
                Order order = orderDao.selectOrderById(orderId);

                if (order == null) {
                    json = "{\"success\": false, \"message\": \"Không tìm thấy đơn hàng.\"}";
                } else if (order.getDigitalSignature() == null || order.getDigitalSignature().trim().isEmpty()) {
                    json = "{\"success\": false, \"message\": \"Đơn hàng chưa được ký.\"}";
                } else {
                    boolean isValid = CheckOrder.checkOrder(order);
                    if (isValid) {
                        json = "{\"success\": true, \"message\": \"Đơn hàng không bị thay đổi.\"}";
                    } else {
                        json = "{\"success\": false, \"message\": \"Đơn hàng có thể đã bị thay đổi.\"}";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json = "{\"success\": false, \"message\": \"Lỗi"+ "\"}";
        }

        response.getWriter().write(json);
    }
}