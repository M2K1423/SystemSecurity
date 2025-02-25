package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteProductController", value = "/deleteProduct")
public class DeleteProductController extends HttpServlet {
    private final ProductServices productServices = new ProductServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println(action);
        if(action.equals("delete-product")) {
            deleteProduct(request, response);
        }else if(action.equals("show-detail")) {
            showProductDetail(request, response);
        }

    }

    private void showProductDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("page");
        String idParam = request.getParameter("id");

        System.out.println(page);
        System.out.println(idParam);

        try {
            int id = Integer.parseInt(idParam);
            ProductDetail productDetail = productServices.getProductDetailById(id);
            System.out.println(productDetail);
            if (productDetail == null) {
                response.sendRedirect("error.jsp?message=Sản phẩm không tồn tại");
            } else {
                request.setAttribute("productViewDetail", productDetail);
                request.getRequestDispatcher("admin/product-management.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp?message=ID không hợp lệ");
        }
    }

    public void deleteProduct(HttpServletRequest request, HttpServletResponse response){
        try {
            // 1. Lấy `id` của sản phẩm từ request
            int productId = Integer.parseInt(request.getParameter("id"));

            // 3. Xóa sản phẩm bằng ProductService
            boolean isDeleted = productServices.deleteProduct(productId);

            // 4. Xử lý phản hồi
            if (isDeleted) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\": \"success\", \"message\": \"Xóa sản phẩm thành công!\"}");
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Không thể xóa sản phẩm!\"}");
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Đã xảy ra lỗi khi xóa sản phẩm!\"}");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
