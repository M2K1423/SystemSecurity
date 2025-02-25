package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.Category;
import com.example.webbongden.dao.model.SubCategory;
import com.example.webbongden.services.CategorySevices;
import com.example.webbongden.services.ProductServices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AddCateController", value = "/categories")
public class AddCateController extends HttpServlet {
    private final CategorySevices categorySevices = new CategorySevices();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Lấy danh sách tất cả các danh mục
        List<Category> categories = categorySevices.getAllCategories();

        // Chuyển danh sách thành JSON và trả về
        String json = new Gson().toJson(categories);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
