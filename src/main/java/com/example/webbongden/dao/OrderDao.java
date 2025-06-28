package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Customer;
import com.example.webbongden.dao.model.Invoices;
import com.example.webbongden.dao.model.Order;
import com.example.webbongden.dao.model.OrderDetail;
import org.jdbi.v3.core.Jdbi;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class OrderDao {
    private final Jdbi jdbi;

    public OrderDao() {
        jdbi = JDBIConnect.get();
    }

    public void updateOrderStatus(int orderId, boolean isSigned) {
        jdbi.useHandle(handle -> {
            String sql = "UPDATE orders SET is_signed = :isSigned WHERE id = :orderId";
            int rowsAffected = handle.createUpdate(sql)
                    .bind("isSigned", isSigned)
                    .bind("orderId", orderId)
                    .execute();
            System.out.println("Rows affected: " + rowsAffected);
        });
    }

    public void updateDigitalSignature(int orderId, String signatureBase64, String certBase64, String hashBase64) {
        String sql = "UPDATE orders SET digital_signature = :signature, digital_cert = :cert, hash_value = :hash WHERE id = :id";
        try {
            jdbi.useHandle(handle ->
                    handle.createUpdate(sql)
                            .bind("signature", signatureBase64)
                            .bind("cert", certBase64)
                            .bind("hash", hashBase64)
                            .bind("id", orderId)
                            .execute()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateOrderHash(int orderId, String hashValue) {
        String sql = "UPDATE orders SET hash_value = :hash WHERE id = :id";
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("hash", hashValue)
                        .bind("id", orderId)
                        .execute()
        );
    }


    public Order getOrderById(int orderId) {
        String sql = """
        SELECT
            o.id AS orderId,
            o.created_at AS orderDate,
            o.total_price AS totalPrice,
            o.order_status AS orderStatus,
            o.hash_value AS hashValue,
            o.digital_cert AS digitalCert,
            o.digital_signature AS digitalSignature,
            o.is_signed, -- Không alias, giữ nguyên tên cột gốc
            c.cus_name AS customerName,
            s.address AS shippingAddress,
            pk.public_key AS publicKey,
            o.account_id AS accountId,
            pk.id AS pkId
        FROM orders o
        JOIN accounts a ON o.account_id = a.id
        JOIN customers c ON a.customer_id = c.id
        JOIN shipping s ON o.id = s.order_id
        JOIN public_keys pk ON o.pk_id = pk.id
        WHERE o.id = :orderId
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setId(rs.getInt("orderId"));
                            order.setCreatedAt(rs.getDate("orderDate"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setOrderStatus(rs.getString("orderStatus"));
                            order.setHashValue(rs.getString("hashValue"));
                            order.setDigitalCert(rs.getString("digitalCert"));
                            order.setDigitalSignature(rs.getString("digitalSignature"));
                            order.setCustomerName(rs.getString("customerName"));
                            order.setAddress(rs.getString("shippingAddress"));
                            order.setPkId(rs.getInt("pkId"));
                            order.setPublicKey(rs.getString("publicKey"));
                            order.setAccountId(rs.getInt("accountId"));
                            // Lấy đúng cột is_signed không alias
                            boolean isSigned = rs.getBoolean("is_signed");
                            System.out.println("Debug getOrderById - orderId=" + order.getId() + ", isSigned=" + isSigned);
                            order.setSigned(isSigned);

                            return order;
                        })
                        .findOne()
                        .orElse(null)
        );
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
                "       o.order_status AS status, " +
                "       o.is_signed AS isSigned " +
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "WHERE o.created_at BETWEEN " +
                "      DATE_FORMAT(NOW(), '%Y-%m-01') " +
                "      AND LAST_DAY(NOW())";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Order(
                                rs.getInt("orderId"),
                                rs.getString("customerName"),
                                rs.getDate("orderDate"),
                                rs.getString("status"),
                                rs.getBoolean("isSigned")
                        ))
                        .list()
        );
    }


    public List<Order> getListOrders() {
        System.out.println("getListOrders() called");
        String sql = "SELECT o.id AS orderId, c.cus_name AS customerName, " +
                "o.created_at AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "s.address AS shippingAddress, o.order_status AS status, o.is_signed AS isSigned " +
                "FROM orders o " +
                "JOIN accounts a ON o.account_id = a.id " +
                "JOIN customers c ON a.customer_id = c.id " +
                "JOIN shipping s ON o.id = s.order_id";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> {
                            Order order = new Order(
                                    rs.getInt("orderId"),
                                    rs.getString("customerName"),
                                    rs.getDate("orderDate"),
                                    rs.getDouble("totalPrice"),
                                    rs.getString("shippingAddress"),
                                    rs.getString("status"),
                                    getOrderDetailsByOrderId(rs.getInt("orderId"))
                            );

                            boolean isSigned = rs.getBoolean("isSigned");
                            System.out.println("Debug orderId=" + rs.getInt("orderId") + ", isSigned=" + isSigned);
                            order.setSigned(isSigned);

                            return order;
                        })
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
    public int createOrderFromInvoice(Invoices invoice, Customer customer, int pkId) {
        String sql = "INSERT INTO orders (invoice_id, account_id, created_at, total_price, note, order_status, shipping_fee, shipping_method, recipient_name, recipient_phone, pk_id) " +
                "VALUES (:invoiceId, :accountId, :createdAt, :totalPrice, :note, 'Pending', 0, 'COD', :recipientName, :recipientPhone, :pkId)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("invoiceId", invoice.getId())
                        .bind("accountId", invoice.getAccountId())
                        .bind("createdAt", invoice.getCreatedAt())
                        .bind("totalPrice", invoice.getTotalPrice())
                        .bind("note", customer.getNote())
                        .bind("recipientName", customer.getCusName())
                        .bind("recipientPhone", customer.getPhone())
                        .bind("pkId", pkId)
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
        OrderDao orderDao = new OrderDao();

        List<Order> orders = orderDao.getOrdersInLastMonth();

        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId());
            System.out.println("Customer Name: " + order.getCustomerName());
            System.out.println("Created At: " + order.getCreatedAt());
            System.out.println("Order Status: " + order.getOrderStatus());
            System.out.println("isSigned: " + order.isSigned());
            System.out.println("---------------------------");
        }
    }

    public Order selectOrderById(int orderId) {
        String sql = """
        SELECT
            o.id AS orderId,
            o.created_at AS orderDate,
            o.total_price AS totalPrice,
            o.order_status AS orderStatus,
            o.note AS note,
            o.shipping_fee AS shippingFee,
            o.shipping_method AS shippingMethod,
            o.hash_value AS hashValue,
            o.digital_cert AS digitalCert,
            o.digital_signature AS digitalSignature,
            o.is_signed, -- Không alias, giữ nguyên tên cột gốc
            o.recipient_name AS recipientName,
            o.recipient_phone AS recipientPhone,
            o.pk_id AS pkId,
            s.address AS shippingAddress,
            pk.public_key AS publicKey,
            o.account_id AS accountId
        FROM orders o
        JOIN shipping s ON o.id = s.order_id
        JOIN public_keys pk ON o.pk_id = pk.id
        WHERE o.id = :orderId
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setId(rs.getInt("orderId"));
                            order.setCreatedAt(rs.getDate("orderDate"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setNote(rs.getString("note"));
                            order.setOrderStatus(rs.getString("orderStatus"));
                            order.setShippingFee(rs.getDouble("shippingFee"));
                            order.setShippingMethod(rs.getString("shippingMethod"));
                            order.setHashValue(rs.getString("hashValue"));
                            order.setDigitalCert(rs.getString("digitalCert"));
                            order.setDigitalSignature(rs.getString("digitalSignature"));
                            order.setCustomerName(rs.getString("recipientName"));
                            order.setPhone(rs.getString("recipientPhone"));
                            order.setAddress(rs.getString("shippingAddress"));
                            order.setPkId(rs.getInt("pkId"));
                            order.setPublicKey(rs.getString("publicKey"));
                            order.setAccountId(rs.getInt("accountId"));
                            // Lấy đúng cột is_signed không alias
                            boolean isSigned = rs.getBoolean("is_signed");
                            System.out.println("Debug getOrderById - orderId=" + order.getId() + ", isSigned=" + isSigned);
                            order.setSigned(isSigned);

                            return order;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    // Cập nhật chữ kí điện tử trong trang chi tiết đơn hàng
    public int updateDigitalSignature(int orderId, String digitalSignature) {
        String sql = """
                UPDATE orders SET digital_signature = :digital_signature
                WHERE id = :orderId
                """;
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("digital_signature", digitalSignature)
                        .bind("orderId", orderId)
                        .execute()
        );
    }

    public String getPublicKeyByOrderId(int orderId) {
        String sql = """
            SELECT public_key FROM public_keys JOIN orders ON orders.pk_id = public_keys.id WHERE orders.id = :orderId
            """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }

}






