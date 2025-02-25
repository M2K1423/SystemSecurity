package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.ProductImage;
import com.example.webbongden.dao.model.Promotion;
import com.example.webbongden.dao.model.Product;
import org.jdbi.v3.core.Jdbi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PromotionDao {
    private final Jdbi jdbi;

    public PromotionDao() {
        this.jdbi = JDBIConnect.get();
    }


    public Promotion getPromotionByProductId(int productId) {
        String sql = "SELECT * FROM promotions " +
                "WHERE product_id = :productId " +
                "AND start_day <= CURDATE() " + // CURDATE() là hàm MySQL để lấy ngày hiện tại
                "AND end_day >= CURDATE()";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId) // Gán tham số productId
                        .mapToBean(Promotion.class)  // Ánh xạ kết quả sang đối tượng Promotion
                        .findOne()
                        .orElse(null) // Nếu không tìm thấy thì trả về null
        );
    }

    //Thêm chương trình mới
    public boolean addPromotion(Promotion promotion) {
        String sql = "INSERT INTO promotions (promotion_name, start_day, end_day, discount_percent, promotion_type) " +
                "VALUES (:name, NOW(), :endDate, :discount, :type)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("name", promotion.getPromotionName())
                        .bind("endDate", promotion.getEndDay())
                        .bind("discount", promotion.getDiscountPercent())
                        .bind("type", promotion.getPromotionType())
                        .execute() > 0
        );
    }


    // Lấy ra danh sách chương trinh giam gia lon
    public List<Promotion> getPromotionsWithoutProduct() {
        String sql = "SELECT * FROM promotions " +
                "WHERE product_id IS NULL " +
                "ORDER BY start_day DESC"; // Có thể sắp xếp theo ngày bắt đầu

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Promotion.class)
                        .list() // Trả về danh sách
        );
    }

    //Thêm sp vào chương trình khuyến mãi
    public boolean addProductToPromotion(int promotionId, int productId) {
        String checkSql = "SELECT COUNT(*) FROM promotion_programs WHERE promotion_id = :promotionId AND product_id = :productId";
        String insertSql = "INSERT INTO promotion_programs (promotion_id, product_id) VALUES (:promotionId, :productId)";

        return jdbi.withHandle(handle -> {
            // Kiểm tra xem sản phẩm đã tồn tại trong chương trình chưa
            int count = handle.createQuery(checkSql)
                    .bind("promotionId", promotionId)
                    .bind("productId", productId)
                    .mapTo(Integer.class)
                    .one();

            if (count > 0) {
                // Nếu sản phẩm đã tồn tại trong chương trình, không thêm nữa
                return false;
            }

            // Nếu chưa tồn tại, tiến hành thêm vào
            return handle.createUpdate(insertSql)
                    .bind("promotionId", promotionId)
                    .bind("productId", productId)
                    .execute() > 0;
        });
    }

    public List<Product> getProductsByPromotionId(int promotionId) {
        String sql = "SELECT p.id, p.product_name, p.unit_price " +
                "FROM products p " +
                "JOIN promotion_programs pp ON p.id = pp.product_id " +
                "WHERE pp.promotion_id = :promotionId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("promotionId", promotionId)
                        .map((rs, ctx) -> new Product(
                                rs.getInt("id"),
                                rs.getString("product_name"),
                                rs.getDouble("unit_price")
                        ))
                        .list()
        );
    }

    public List<Promotion> getAllPromotionsWithProducts() {
        String sql = "SELECT " +
                "pr.id AS promotion_id, " +
                "pr.promotion_name, " +
                "pr.start_day, " +
                "pr.end_day, " +
                "pr.discount_percent AS promotion_discount_percent, " +
                "pr.promotion_type, " +
                "p.id AS product_id, " +
                "p.product_name, " +
                "p.unit_price, " +
                "p.discount_percent AS product_discount_percent, " +
                "pi.url AS image_url, " +
                "pi.main_image " +
                "FROM promotions pr " +
                "JOIN promotion_programs pp ON pr.id = pp.promotion_id " +
                "JOIN products p ON pp.product_id = p.id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "ORDER BY pr.id, p.created_at DESC";

        return jdbi.withHandle(handle -> {
            Map<Integer, Promotion> promotionMap = new LinkedHashMap<>();

            handle.createQuery(sql)
                    .map((rs, ctx) -> {
                        int promotionId = rs.getInt("promotion_id");
                        Promotion promotion = promotionMap.get(promotionId);

                        // Nếu chương trình khuyến mãi chưa tồn tại, tạo mới
                        if (promotion == null) {
                            promotion = new Promotion(
                                    promotionId,
                                    rs.getString("promotion_name"),
                                    Date.valueOf(rs.getDate("start_day").toLocalDate()), // Chuyển đổi LocalDate -> Date
                                    Date.valueOf(rs.getDate("end_day").toLocalDate()),   // Chuyển đổi LocalDate -> Date
                                    rs.getDouble("promotion_discount_percent"),
                                    rs.getString("promotion_type"),
                                    new ArrayList<>()
                            );
                            promotionMap.put(promotionId, promotion);
                        }

                        // Xử lý sản phẩm
                        int productId = rs.getInt("product_id");
                        Product product = promotion.getProducts().stream()
                                .filter(p -> p.getId() == productId)
                                .findFirst()
                                .orElse(null);

                        if (product == null) {
                            product = new Product(
                                    productId,
                                    rs.getString("product_name"),
                                    rs.getDouble("unit_price"),
                                    rs.getDouble("product_discount_percent"),
                                    new ArrayList<>()
                            );
                            promotion.getProducts().add(product);
                        }

                        // Xử lý hình ảnh sản phẩm
                        String imageUrl = rs.getString("image_url");
                        if (imageUrl != null) {
                            product.getListImg().add(new ProductImage(
                                    imageUrl,
                                    rs.getBoolean("main_image")
                            ));
                        }

                        return promotion;
                    }).list();

            return new ArrayList<>(promotionMap.values());
        });
    }

    public boolean deleteProductFromPromotion(int promotionId, int productId) {
        String sql = "DELETE FROM promotion_programs WHERE promotion_id = :promotionId AND product_id = :productId";

        int rowsAffected = jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("promotionId", promotionId)
                        .bind("productId", productId)
                        .execute()
        );

        return rowsAffected > 0; // Trả về true nếu xóa thành công
    }

    public boolean deletePromotionById(int promotionId) {
        String sql = "DELETE FROM promotions WHERE id = :promotionId";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("promotionId", promotionId)
                        .execute() > 0
        );
    }
}
