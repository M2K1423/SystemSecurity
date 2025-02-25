package com.example.webbongden.controller.AdminController.Dashboard;

import com.example.webbongden.dao.model.TopProduct;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.UserSevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ĐashBoardController", value = "/dashboard")
public class DashBoardController extends HttpServlet {
    private static final ProductServices productServices;

    static {
        productServices = new ProductServices();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy danh sách Top 5 sản phẩm bán chạy nhất
        List<TopProduct> topProducts = productServices.getTopProducts();


            // Đưa dữ liệu vào request attribute
            request.setAttribute("topProducts", topProducts);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/dashboard.jsp");
            dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
