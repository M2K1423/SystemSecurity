package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.OrderDao;
import com.example.webbongden.dao.model.Order;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

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

        String filename = "invoice_" + orderId + ".txt";
        String content = "Order ID: " + order.getId() + "\n"
                + "Customer: " + order.getCustomerName() + "\n"
                + "Date: " + order.getCreatedAt() + "\n"
                + "Total: " + order.getTotalPrice() + "\n";

        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment;filename=" + filename);

        OutputStream os = resp.getOutputStream();
        os.write(content.getBytes());
        os.flush();
        os.close();
    }
}

