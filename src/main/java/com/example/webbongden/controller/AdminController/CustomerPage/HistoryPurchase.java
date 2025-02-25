package com.example.webbongden.controller.AdminController.CustomerPage;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.services.UserSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistoryPurchase", value = "/customer-orders")
public class HistoryPurchase extends HttpServlet {
    private static final UserSevices userSevices;

    static {
        userSevices = new UserSevices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy customerId từ tham số request
        String customerIdParam = request.getParameter("customerId");

        if (customerIdParam == null || customerIdParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid customerId");
            return;
        }

        try {
            int customerId = Integer.parseInt(customerIdParam.trim());

            // Gọi service để lấy danh sách đơn hàng của khách hàng
            List<Order> orders = userSevices.getPurchaseHistoryByCustomerId2(customerId);

            // Trả về JSON sử dụng ObjectMapper
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(orders));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid customerId format");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving purchase history");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
