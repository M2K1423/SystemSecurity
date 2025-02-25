package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.PromotionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GetProductByPromotion", value = "/get-products-by-promotion")
public class GetProductByPromotion extends HttpServlet {
    private static final PromotionService promotionService = new PromotionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = new HashMap<>();

        try {
            // Lấy promotionId từ request
            int promotionId = Integer.parseInt(request.getParameter("promotionId"));

            // Gọi service để lấy danh sách sản phẩm
            List<Product> products = promotionService.getProductsByPromotionId(promotionId);

            if (products != null && !products.isEmpty()) {
                responseBody.put("success", true);
                responseBody.put("data", products);
            } else {
                responseBody.put("success", false);
                responseBody.put("message", "Không có sản phẩm nào trong chương trình khuyến mãi này.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseBody.put("success", false);
            responseBody.put("message", "Đã xảy ra lỗi trong quá trình xử lý.");
            responseBody.put("error", e.getMessage());
            e.printStackTrace();
        }

        // Gửi phản hồi JSON
        mapper.writeValue(response.getWriter(), responseBody);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
