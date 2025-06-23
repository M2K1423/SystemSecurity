        package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.OrderDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@WebServlet(name = "DownloadOrderServlet", urlPatterns = "/download-order")
public class DownloadOrderServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        if (orderId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is required");
            return;
        }

        // Fetch order details
        Order order = orderDao.getOrderById(Integer.parseInt(orderId));
        List<OrderDetail> orderDetails = orderDao.getOrderDetailsByOrderId(Integer.parseInt(orderId));

        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        // Định dạng ngày yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format file content đúng chuẩn CheckOrder.generateRawData
        StringBuilder content = new StringBuilder();
        content.append(order.getCustomerName().trim()).append("\r\n")
                .append(order.getPhone()).append("\r\n")
                .append(order.getId()).append("\r\n")
                .append(sdf.format(order.getCreatedAt())).append("\r\n")
                .append(order.getAddress()).append("\r\n")
                .append(order.getShippingMethod()).append("\r\n")
                .append(String.format("%.2f", order.getShippingFee())).append("\r\n");

        for (OrderDetail detail : orderDetails) {
            content.append(detail.getProductId()).append("\r\n")
                    .append(detail.getProductName()).append("\r\n")
                    .append(detail.getQuantity()).append("\r\n")
                    .append(detail.getUnitPrice()).append("\r\n")
                    .append(detail.getAmount()).append("\r\n");
        }

        // Set response headers
        response.setContentType("text/plain; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=order_" + orderId + ".txt");

        // Write content to response
        try (OutputStream out = response.getOutputStream()) {
            out.write(content.toString().getBytes("UTF-8"));
            out.flush();
        }
    }
}
