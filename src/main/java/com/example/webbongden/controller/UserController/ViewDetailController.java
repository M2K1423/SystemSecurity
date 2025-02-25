package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Product;
import com.example.webbongden.dao.model.ProductDetail;
import com.example.webbongden.dao.model.Review;
import com.example.webbongden.services.ProductServices;
import com.example.webbongden.services.RevenueServices;
import com.example.webbongden.services.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewDetailController", value = "/product-detail")
public class ViewDetailController extends HttpServlet {
    private static final ProductServices productServices;
    private static final ReviewService reviewService;
    static {
        productServices = new ProductServices();
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String productIdParam = request.getParameter("id");

            int productId = Integer.parseInt(productIdParam);


            ProductDetail productDetail = productServices.getProductDetailById(productId);
            List<Review> reviews = reviewService.getReviewsByProductId(productId);
            List<String> listImg = productServices.getAllProductUrls(productId);
            String breadCum = productServices.getCategoryNameByProductId(productId);
            List<Product> relatedProducts = productServices.fetchRelatedProducts(productId);

            
            if (productDetail == null) {
                response.sendRedirect("/home?error=product_not_found");
                return;
            }

            // Set product details in the request scope
            request.setAttribute("reviews", reviews);
            request.setAttribute("productDetail", productDetail);
            request.setAttribute("listImg", listImg);
            request.setAttribute("breadCum", breadCum);
            request.setAttribute("relatedProducts", relatedProducts);

            // Forward to the product detail JSP
            request.getRequestDispatcher("/user/product-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/home?error=internal_error");
        }
    }
}
