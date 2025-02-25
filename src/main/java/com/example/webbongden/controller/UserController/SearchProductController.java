package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.ProductServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchProductController", value = "/search")
public class SearchProductController extends HttpServlet {

    private static final ProductServices productService = new ProductServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("value");

        if (query == null || query.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập từ khóa tìm kiếm.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        List<Product> searchResults = productService.searchProductByName(query.trim());

        if (searchResults == null || searchResults.isEmpty()) {
            request.setAttribute("error", "Không tìm thấy sản phẩm nào phù hợp với từ khóa: " + query);
        } else {
            request.setAttribute("searchResults", searchResults);
        }

        request.setAttribute("query", query.trim());
        request.getRequestDispatcher("/user/search-product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method is not supported for this endpoint.");
    }
}
