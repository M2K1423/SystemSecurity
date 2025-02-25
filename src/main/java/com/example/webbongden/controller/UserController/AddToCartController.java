package com.example.webbongden.controller.UserController;

import com.example.webbongden.dao.model.Cart;
import com.example.webbongden.dao.model.CartItem;
import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AddToCartController", value = "/add-to-cart")
public class AddToCartController extends HttpServlet {
    private static final ProductServices productService;

    static {
        productService = new ProductServices();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);

        }

        // Lấy productId từ request
        String productIdParam = request.getParameter("productId");
        if (productIdParam == null || productIdParam.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Product ID is missing\"}");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            if (!productService.isProductInStock(productId)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Sản phẩm này đã hết hàng.\"}");
                return;
            }
            // Lấy thông tin sản phẩm từ ProductService
            Product product = productService.getProductById(productId);

            if (product != null) {
                // Thêm sản phẩm vào giỏ hàng
                CartItem item = new CartItem(
                        product.getId(),
                        product.getProductName(),
                        1,
                        product.getUnitPrice(),
                        product.getDiscountedPrice(),
                        product.getImageUrl()
                );
                cart.addItem(item);
            }

            // Tính tổng số lượng sản phẩm trong giỏ
            int totalQuantity = cart.getTotalQuantity();

            // Trả về phản hồi JSON thủ công
            String jsonResponse = "{\"status\":\"success\",\"cartQuantity\":" + totalQuantity + "}";
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);

        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid Product ID format\"}");
        }
    }
}
