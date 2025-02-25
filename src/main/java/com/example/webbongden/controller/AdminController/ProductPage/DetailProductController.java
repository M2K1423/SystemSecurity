package com.example.webbongden.controller.AdminController.ProductPage;

import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@WebServlet(name = "DetailProductController", value = "/getProductDetails")
public class DetailProductController extends HttpServlet {
    private static final ProductServices productServices;

    static {
        productServices = new ProductServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Lấy ID sản phẩm từ request
            String productId = request.getParameter("id");
            ProductDetail product = productServices.getProductDetailById(Integer.parseInt(productId));

            // Kiểm tra nếu sản phẩm không tồn tại
            if (product == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Sản phẩm không tồn tại\"}");
                return;
            }

            // Sử dụng ObjectMapper để chuyển đổi đối tượng thành JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            String productJson = objectMapper.writeValueAsString(product);

            // Trả về dữ liệu JSON
            response.getWriter().write(productJson);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Lỗi khi lấy thông tin sản phẩm\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
