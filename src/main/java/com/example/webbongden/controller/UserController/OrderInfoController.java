package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.OrderDetail;
import com.example.webbongden.services.OrderSevices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderInfoController", value = "/order-info")
public class OrderInfoController extends HttpServlet {
    private static final OrderSevices orderServices;

    static {
        orderServices = new OrderSevices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy orderId từ request
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing orderId parameter\"}");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);

            // Gọi service để lấy danh sách OrderDetail và order
            List<OrderDetail> orderDetails = orderServices.getOrderDetailsById(orderId);
            Order order = orderServices.selectOrderById(orderId);
            // Chuyển đổi đơn hàng sang JSON và gửi về client
            Map<String, Object> result = new HashMap<>();
            result.put("orderDetails", orderDetails);
            result.put("order", order);

            String json = new Gson().toJson(result);
            response.getWriter().write(json);


        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid orderId format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Unable to fetch order details\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
    }
}