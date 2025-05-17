package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@WebServlet("/download-invoice")
public class DownloadInvoiceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderId = Integer.parseInt(req.getParameter("id"));

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getOrderById(orderId);

        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng.");
            return;
        }

        // ✅ Format lại đúng rawData để ký
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String totalStr = String.format("%.2f", order.getTotalPrice());
        String customerName = order.getCustomerName().trim();

        String rawData = order.getId() + customerName + totalStr + sdf.format(order.getCreatedAt());

        String filename = "invoice_" + orderId + ".txt";

        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment;filename=" + filename);

        try (OutputStream os = resp.getOutputStream()) {
            os.write(rawData.getBytes(StandardCharsets.UTF_8));
        }
    }
}
