package com.example.webbongden.controller.AdminController.OrderPage;

import com.example.webbongden.services.OrderSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "UpdateOrderController", value = "/update-order-status")
public class UpdateOrderController extends HttpServlet {
    private static final OrderSevices orderServices;

    static {
        orderServices = new OrderSevices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc dữ liệu JSON từ request
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> data = mapper.readValue(request.getReader(), Map.class);

            int orderId = Integer.parseInt(data.get("orderId"));
            String status = data.get("status");

            // Log để kiểm tra dữ liệu nhận được
            System.out.println("Order ID: " + orderId);
            System.out.println("Status: " + status);

            // Cập nhật trạng thái đơn hàng
            boolean updated = orderServices.updateOrderStatus(orderId, status);

            if (updated) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Trạng thái đơn hàng đã được cập nhật\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"message\": \"Không thể cập nhật trạng thái\"}");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Dữ liệu không hợp lệ\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Lỗi hệ thống\"}");
        }
    }
}
