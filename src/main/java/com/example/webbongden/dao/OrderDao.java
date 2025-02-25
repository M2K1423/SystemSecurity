package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Customer;
import com.example.webbongden.dao.model.Invoices;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.OrderDetail;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OrderDao {
    private final Jdbi jdbi;

    public OrderDao() {
        jdbi = JDBIConnect.get();
    }

    public int totalOrderInLastedMonth() {
        String sql = "SELECT COUNT(*) " +
                "FROM orders " +
                "WHERE created_at BETWEEN " +
                "DATE_FORMAT(NOW(), '%Y-%m-01') " + // Ngày đầu tháng hiện tại
                "AND LAST_DAY(NOW())"; // Ngày cuối tháng hiện tại

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int totalPendingOrders() {
        String sql = "SELECT COUNT(*) " +
                "FROM orders " +
                "WHERE order_status = 'pending'";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int totalShippingOrders() {
        String sql = "SELECT COUNT(*) " +
                "FROM orders " +
                "WHERE order_status = 'shipping'";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<Order> getOrdersInLastMonth() {
        String sql = "SELECT o.id AS orderId, " +
                "       c.cus_name AS customerName, " +
                "       o.created_at AS orderDate, " +
                "       o.order_status AS status " +
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "WHERE o.created_at BETWEEN " +
                "      DATE_FORMAT(NOW(), '%Y-%m-01') " + // Ngày đầu tháng hiện tại
                "      AND LAST_DAY(NOW())"; // Ngày cuối tháng hiện tại

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                rs.getString("customerName"),
                                rs.getDate("orderDate"),
                                rs.getString("status")
                        ))
                        .list()
        );
    }

    public List<Order> getListOrders() {
        String sql = "SELECT o.id AS orderId, c.cus_name AS customerName, " +
                "o.created_at AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "s.address AS shippingAddress, o.order_status AS status " + // Lấy địa chỉ từ bảng shipping
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "JOIN shipping s ON o.id = s.order_id"; // Kết hợp bảng shipping

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                rs.getString("customerName"),
                                rs.getDate("orderDate"),
                                rs.getDouble("totalPrice"), // Sử dụng getDouble
                                rs.getString("shippingAddress"), // Lấy địa chỉ vận chuyển từ bảng shipping
                                rs.getString("status"),
                                getOrderDetailsByOrderId(rs.getInt("orderId")) // Lấy danh sách chi tiết đơn hàng
                        ))
                        .list()
        );
    }


    // Lấy danh sách chi tiết đơn hàng theo orderId
        public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
            String sql = "SELECT od.product_id AS productId, " +
                    "       od.order_id AS orderId, " +
                    "       p.product_name AS productName, " +
                    "       od.quantity AS quantity, " +
                    "       od.unit_price AS unitPrice, " +
                    "       od.item_discount AS itemDiscount, " +
                    "       od.amount AS amount " +
                    "FROM order_details od " +
                    "JOIN products p ON od.product_id = p.id " +
                    "WHERE od.order_id = :orderId";

            return jdbi.withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("orderId", orderId)
                            .map((rs, ctx) -> new OrderDetail(
                                    rs.getInt("productId"),
                                    rs.getInt("orderId"),
                                    rs.getString("productName"), // Lấy tên sản phẩm
                                    rs.getInt("quantity"),
                                    rs.getDouble("unitPrice"),
                                    rs.getDouble("itemDiscount"),
                                    rs.getDouble("amount")
                            ))
                            .list()
            );
        }


    public List<Order> getOrdersByKeyword(String keyword) {
        String sql = "SELECT o.id AS orderId, c.cus_name AS customerName, " +
                "o.created_at AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "s.address AS shippingAddress, o.order_status AS status " + // Lấy địa chỉ từ bảng shipping
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "JOIN shipping s ON o.id = s.order_id "+
                "WHERE (:keyword IS NULL OR c.cus_name LIKE :keyword OR o.order_status LIKE :keyword)";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword != null ? "%" + keyword + "%" : null) // Tìm kiếm dựa vào keyword
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                rs.getString("customerName"),
                                rs.getDate("orderDate"),
                                rs.getDouble("totalPrice"),
                                rs.getString("shippingAddress"),
                                rs.getString("status"),
                                getOrderDetailsByOrderId(rs.getInt("orderId")) // Lấy danh sách chi tiết đơn hàng
                        ))
                        .list()
        );
    }

    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET order_status = :status WHERE id = :orderId";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("status", status)
                        .bind("orderId", orderId)
                        .execute() > 0
        );
    }

    public List<Order> filterOrderByStatus(String keyword) {
        String sql = "SELECT o.id AS orderId, c.cus_name AS customerName, " +
                "o.created_at AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "s.address AS shippingAddress, o.order_status AS status " + // Lấy địa chỉ từ bảng shipping
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "JOIN shipping s ON o.id = s.order_id "+
                "WHERE (:keyword IS NULL OR o.order_status LIKE :keyword)";


        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword != null ? "%" + keyword + "%" : null) // Tìm kiếm dựa vào keyword
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                rs.getString("customerName"),
                                rs.getDate("orderDate"),
                                rs.getDouble("totalPrice"),
                                rs.getString("shippingAddress"),
                                rs.getString("status"),
                                getOrderDetailsByOrderId(rs.getInt("orderId")) // Lấy danh sách chi tiết đơn hàng
                        ))
                        .list()
        );
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_price) AS totalRevenue FROM orders";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(double.class)
                        .one()
        );
    }


    //Tạo hóa đơn
    public int createOrderFromInvoice(Invoices invoice, Customer customer) {
        String sql = "INSERT INTO orders (invoice_id, account_id, created_at, total_price, order_status, shipping_fee, shipping_method) " +
                "VALUES (:invoiceId, :accountId, :createdAt, :totalPrice, 'Pending', NULL, NULL)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("invoiceId", invoice.getId())
                        .bind("accountId", invoice.getAccountId())
                        .bind("createdAt", invoice.getCreatedAt())
                        .bind("totalPrice", invoice.getTotalPrice())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );
    }

    public void createOrderDetails(int orderId, List<OrderDetail> orderDetails) {
        String sql = "INSERT INTO order_details (order_id, product_id, quantity, unit_price, item_discount, amount) " +
                "VALUES (:orderId, :productId, :quantity, :unitPrice, :itemDiscount, :amount)";

        jdbi.useHandle(handle -> {
            for (OrderDetail detail : orderDetails) { // Lặp qua từng chi tiết đơn hàng
                // Kiểm tra xem bản ghi đã tồn tại chưa
                boolean exists = handle.createQuery("SELECT COUNT(*) FROM order_details WHERE order_id = :orderId AND product_id = :productId")
                        .bind("orderId", orderId)
                        .bind("productId", detail.getProductId())
                        .mapTo(int.class)
                        .one() > 0;

                if (!exists) {
                    // Nếu chưa tồn tại, thêm mới
                    handle.createUpdate(sql)
                            .bind("orderId", orderId)
                            .bind("productId", detail.getProductId())
                            .bind("quantity", detail.getQuantity())
                            .bind("unitPrice", detail.getUnitPrice())
                            .bind("itemDiscount", detail.getItemDiscount())
                            .bind("amount", detail.getAmount())
                            .execute();
                } else {
                    // Nếu tồn tại, in log để kiểm tra
                    System.out.println("Duplicate entry found for order_id=" + orderId + ", product_id=" + detail.getProductId());
                }
            }
        });
    }

    public List<Order> getOrdersByUsername(String username) {
        String sql = "SELECT " +
                "o.id AS orderId, " +
                "o.created_at AS orderDate, " +
                "o.order_status AS orderStatus, " + // Lấy trạng thái đơn hàng
                "o.total_price AS totalPrice " +
                "FROM accounts a " +
                "JOIN orders o ON a.id = o.account_id " +
                "WHERE a.username = :username " +
                "ORDER BY o.created_at DESC";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("username", username) // Gắn giá trị username vào câu truy vấn
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                null,
                                rs.getDate("orderDate"),
                                rs.getDouble("totalPrice"),
                                null, // Không dùng tổng số lượng
                                rs.getString("orderStatus"), // Lấy trạng thái đơn hàng
                                null
                        ))
                        .list()
        );
    }


    public static void main(String[] args) {
        // Tạo một đối tượng UserDao (được giả định là chứa phương thức getOrdersByUsername)
        OrderDao userDao = new OrderDao();

        // Nhập tên đăng nhập cần kiểm tra
        String username = "pvp1292004";

        // Gọi phương thức getOrdersByUsername
        List<Order> orders = userDao.getOrdersByUsername(username);

        // In thông tin kết quả
        if (orders != null && !orders.isEmpty()) {
            System.out.println("Danh sách đơn hàng của tài khoản username: " + username);
            for (Order order : orders) {
                System.out.println("ID Đơn hàng: " + order.getId());
                System.out.println("Ngày đặt hàng: " + order.getCreatedAt());
                System.out.println("Tổng tiền: " + order.getTotalPrice());
                System.out.println("Trạng thái: " + order.getOrderStatus());
                System.out.println("-----------------------------------------");
            }
        } else {
            System.out.println("Không tìm thấy đơn hàng nào cho tài khoản username: " + username);
        }
    }
}






