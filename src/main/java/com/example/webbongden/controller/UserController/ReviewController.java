package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Review;
import com.example.webbongden.services.ReviewService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReviewController", value = "/review")
public class ReviewController extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Đọc dữ liệu JSON từ request
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }

            // Parse JSON
            JsonObject jsonData = JsonParser.parseString(jsonBuilder.toString()).getAsJsonObject();

            int productId = jsonData.get("productId").getAsInt();
            int accountId = jsonData.get("accountId").getAsInt();
            String content = jsonData.get("content").getAsString();
            int rating = jsonData.get("rating").getAsInt();

            System.out.println("productId: " + productId);
            System.out.println("accountId: " + accountId);
            System.out.println("content: " + content);
            System.out.println("rating: " + rating);

            String reviewType = "product_review";
            boolean isSuccess = reviewService.addReview(productId, accountId, content, rating, reviewType);

            // Tạo phản hồi JSON
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", isSuccess);
            jsonResponse.addProperty("message", isSuccess ? "Bình luận đã được gửi thành công!" : "Gửi bình luận thất bại.");

            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "Đã xảy ra lỗi trong quá trình xử lý.");
            response.getWriter().write(errorResponse.toString());
        }
    }




    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        request.setAttribute("reviews", reviews);
        request.getRequestDispatcher("/product-detail.jsp").forward(request, response);
    }
}
