package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminLoadProductController", value = "/AdminLoadProductController")
public class AdminLoadProductController extends HttpServlet {
    private static final ProductServices productServices;

    static {
        productServices = new ProductServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tham số tìm kiếm từ request
        String keyword = request.getParameter("searchValue");

        List<Product> productList;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Nếu có từ khóa tìm kiếm, gọi phương thức tìm kiếm
            productList = productServices.getProductsByKeyword(keyword);
        } else {
            // Nếu không có từ khóa, lấy toàn bộ sản phẩm
            productList = productServices.getListProductForAdminPage();
        }

        // Thiết lập kiểu dữ liệu trả về là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Chuyển danh sách sản phẩm thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productList);

        // Gửi JSON về client
        response.getWriter().write(json);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
