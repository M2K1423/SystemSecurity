package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.services.CategorySevices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CategoryAdminController", urlPatterns = {
        "/categories/add",
        "/subcategories/add",
        "/categories/delete/*",
        "/subcategories/delete/*"
})
public class CategoryAdminController extends HttpServlet {
    private final CategorySevices categorySevices = new CategorySevices();

    //Xử lí thêm danh mục, danh mục con
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            switch (request.getServletPath()) {
                case "/categories/add" -> handleAddCategory(request, response);
                case "/subcategories/add" -> handleAddSubCategory(request, response);
                default -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"message\": \"Endpoint không tồn tại.\"}");
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Đã xảy ra lỗi: " + e.getMessage() + "\"}");
        }
    }

    private void handleAddCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String categoryName = request.getParameter("name");
        if (categoryName == null || categoryName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Tên danh mục không được để trống.\"}");
            return;
        }

        if (categorySevices.addCategory(categoryName)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Danh mục cha đã được thêm thành công.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Không thể thêm danh mục cha.\"}");
        }
    }

    private void handleAddSubCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String parentIdStr = request.getParameter("parentId");
        String subCategoryName = request.getParameter("name");

        System.out.println(parentIdStr);
        System.out.println(subCategoryName);
        if (parentIdStr == null || subCategoryName == null || subCategoryName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Dữ liệu không hợp lệ.\"}");
            return;
        }

        try {
            int parentId = Integer.parseInt(parentIdStr);
            if (categorySevices.addSubCategory(parentId, subCategoryName)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Danh mục con đã được thêm thành công.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"message\": \"Không thể thêm danh mục con.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"ID danh mục cha không hợp lệ.\"}");
        }
    }


    //Xử lí xóa
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getRequestURI(); // Lấy toàn bộ URL để phân tích
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo.contains("/categories/delete/")) {
                // Xóa danh mục cha
                String[] pathParts = pathInfo.split("/"); // Phân tách các phần của URL
                int categoryId = Integer.parseInt(pathParts[pathParts.length - 1]); // Lấy ID từ cuối URL
                System.out.println(categoryId);
                boolean isDeleted = categorySevices.deleteCategory(categoryId);
                if (isDeleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Danh mục đã được xóa thành công\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"message\": \"Không thể xóa danh mục vì có danh mục con\"}");
                }
            } else if (pathInfo.contains("/subcategories/delete/")) {
                // Xóa danh mục con
                String[] pathParts = pathInfo.split("/");
                int subCategoryId = Integer.parseInt(pathParts[pathParts.length - 1]);
                boolean isDeleted = categorySevices.deleteSubCategory(subCategoryId);

                if (isDeleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Danh mục con đã được xóa thành công\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"message\": \"Không thể xóa danh mục con\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\": \"Endpoint không tồn tại\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Đã xảy ra lỗi: " + e.getMessage() + "\"}");
        }
    }
}
