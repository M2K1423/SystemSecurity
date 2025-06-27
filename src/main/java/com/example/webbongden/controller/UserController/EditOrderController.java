package com.example.webbongden.controller.UserController;
import com.example.webbongden.websocket.OrderNotificationSocket;

import com.example.webbongden.dao.model.Cart;
import com.example.webbongden.dao.model.CartItem;
import com.example.webbongden.dao.model.OrderDetail;
import com.example.webbongden.dao.model.Product;
import com.example.webbongden.services.EmailService;
import com.example.webbongden.services.OrderSevices;
import com.example.webbongden.services.ProductServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EditOrderController", value = "/edit-order")
public class EditOrderController extends HttpServlet {
    private static final OrderSevices orderServices;
    private static final ProductServices productService;

    static {
        orderServices = new OrderSevices();
        productService = new ProductServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy mã đơn hàng
            String orderIdParam = request.getParameter("orderId");
            if (orderIdParam == null || orderIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu orderId");
                return;
            }
            // Đổi mã đơn hàng thành int
            int orderId = Integer.parseInt(orderIdParam);

            // Lấy danh sách sản phẩm trong đơn hàng
            List<OrderDetail> orderDetails = orderServices.getOrderDetailsById(orderId);
            List<Product> products= new ArrayList<>();
            for(OrderDetail orderDetail : orderDetails) {
                Product product = productService.getProductById(orderDetail.getProductId());
                products.add(product);
            }
            // trường hợp không tìm thấy danh sách sản phẩm
            if (products.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng hoặc đơn hàng trống");
                return;
            }
            // Đặt trạng thái đơn hàng là Huỷ
            orderServices.updateOrderStatus(orderId,"Huỷ");
            OrderNotificationSocket.broadcast("Đơn hàng #" + orderId + " vừa bị huỷ và trả lại vào giỏ hàng!");

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);

            }

            // Thêm từng sản phẩm từ đơn hàng vào giỏ hàng
            for (Product product: products) {
                int quantity=0;
                // lấy số lượng của từng sản phẩm trong chi tiết đơn hàng
                for (OrderDetail orderDetail: orderDetails) {
                    if(orderDetail.getProductId() == product.getId()) {
                        quantity = orderDetail.getQuantity();
                        break;
                    }
                }

                // tạo đối tượng CartItem
                CartItem item = new CartItem(
                        product.getId(),
                        product.getProductName(),
                        quantity,
                        product.getUnitPrice(),
                        product.getDiscountedPrice(),
                        product.getImageUrl()
                );

                // thêm đối tượng CartItem vào giỏ hàng
                cart.addItem(item);
            }

            // Lưu vào session
            session.setAttribute("cart", cart);


            // Chuyển hướng về trang giỏ hàng
            response.sendRedirect(request.getContextPath() + "/cart");

            //gửi email thông báo
            String email = request.getParameter("email");
            String body= buildEmailContent(products, orderDetails);
            EmailService.sendEmailHtml(email, "Thay đôi thông tin đơn hàng" + orderId, body);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "orderId không hợp lệ");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xử lý đơn hàng");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public String buildEmailContent(List<Product> products, List<OrderDetail> orderDetails) {
        StringBuilder sb = new StringBuilder();
        sb.append("Chúng tôi đã nhận được yêu cầu thay đổi thông tin đơn hàng của bạn.");
        sb.append(("Các sản phẩm trong đơn hàng sẽ được đặt lại vào giỏ hàng của bạn. Hãy đặt hàng sau khi bạn thay đổi thông tin thành công"));
        sb.append("<h2>Thông tin đơn hàng của bạn</h2>");
        sb.append("<table border='1' cellpadding='8' cellspacing='0'>");
        sb.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá gốc</th><th>Giá giảm</th><th>Thành tiền</th></tr>");

        for (Product product : products) {
            int quantity = 0;
            for (OrderDetail orderDetail: orderDetails) {
                if(orderDetail.getProductId() == product.getId()) {
                    quantity = orderDetail.getQuantity();
                }
            }
            sb.append("<tr>");
            sb.append("<td>").append(product.getProductName()).append("</td>");
            sb.append("<td>").append(quantity).append("</td>");
            sb.append("<td>").append(product.getUnitPrice()).append("</td>");
            sb.append("<td>").append(product.getDiscountedPrice()).append("</td>");
            sb.append("<td>").append(quantity * product.getDiscountedPrice()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }

}