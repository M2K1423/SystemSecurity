package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.SubCategory;
import com.example.webbongden.services.CategorySevices;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SubCategoryController", value = "/categories/subcategories")
public class SubCategoryController extends HttpServlet {
    private final CategorySevices categorySevices = new CategorySevices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String categoryIdStr = request.getParameter("categoryId");
        int categoryId = Integer.parseInt(categoryIdStr);

        List<SubCategory> subCategories = categorySevices.getSubCategoriesByCategoryId(categoryId);

        String json = new Gson().toJson(subCategories);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
