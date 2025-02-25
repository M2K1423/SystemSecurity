package com.example.webbongden.controller.AdminController.PromotionPage;

import com.example.webbongden.services.PromotionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "AddProductPromotionController", value = "/add-product-to-promotion")
public class AddProductPromotionController extends HttpServlet {
    private static final PromotionService promotionServices;

    static {
        promotionServices = new PromotionService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Đọc JSON từ request body
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonData = jsonBuilder.toString();
            System.out.println("JSON Received: " + jsonData);

            // Parse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> data = objectMapper.readValue(jsonData, Map.class);

            // Lấy tham số từ JSON
            String promotionIdStr = data.get("promotionId");
            String productIdStr = data.get("productId");

            if (promotionIdStr == null || productIdStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Thiếu tham số promotionId hoặc productId.\"}");
                return;
            }

            int promotionId = Integer.parseInt(promotionIdStr);
            int productId = Integer.parseInt(productIdStr);

            // Thêm sản phẩm vào chương trình
            boolean success = promotionServices.addProductToPromotion(promotionId, productId);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if (success) {
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Thêm sản phẩm vào chương trình thành công!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Không thể thêm sản phẩm vào chương trình.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"ID không hợp lệ.\"}");
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Đã xảy ra lỗi.\"}");
            e.printStackTrace();
        }
    }
}
