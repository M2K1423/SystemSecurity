package com.example.webbongden.controller.AdminController.OrderPage;

import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadOrderController", value = "/list-order")
public class LoadOrderController extends HttpServlet {
    private static final OrderSevices orderServices;

    static {
        orderServices = new OrderSevices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> orderList;
        String keyword = request.getParameter("searchValue");
        String status = request.getParameter("status");
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Nếu có từ khóa tìm kiếm, gọi phương thức tìm kiếm
            orderList = orderServices.getOrdersByKeyWord(keyword);
        }else if(status != null && !status.trim().isEmpty()) {
            orderList = orderServices.filterOrderByStatus(status);
        }
        else {
            // Nếu không có từ khóa, lấy toàn bộ sản phẩm
            orderList = orderServices.getAllOrders();;
        }

        // Thiết lập kiểu dữ liệu trả về là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Chuyển danh sách sản phẩm thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(orderList);

        // Gửi JSON về client
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
