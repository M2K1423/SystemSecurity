package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

import java.util.*;

public class ProductDao {
    private final Jdbi jdbi;

    public ProductDao() {
        this.jdbi = JDBIConnect.get();
    }

    public List<Product> getAllProduct() {
        String sql = "SELECT p.id AS product_id, p.product_name, p.unit_price, p.discount_percent, " +
                "pi.url AS image_url, pi.main_image " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "ORDER BY p.created_at DESC "
                ;

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql);
            Map<Integer, Product> productMap = new HashMap<>();

            query.map((rs, ctx) -> {
                int productId = rs.getInt("product_id");
                Product product = productMap.get(productId);

                if (product == null) {
                    product = new Product(
                            productId,
                            rs.getString("product_name"),
                            rs.getDouble("unit_price"),
                            rs.getDouble("discount_percent"),
                            new ArrayList<>()
                    );
                    productMap.put(productId, product);
                }

                String imageUrl = rs.getString("image_url");
                if (imageUrl != null) {
                    product.getListImg().add(new ProductImage(
                            imageUrl,
                            rs.getBoolean("main_image")
                    ));
                }

                return product;
            }).list();

            return new ArrayList<>(productMap.values());
        });
    }

    // Lấy ds sp theo Category
    public List<Product> getProductsByCategory(int categoryId) {
        String sql = "SELECT " +
                "p.id AS product_id, " +
                "p.product_name, " +
                "p.unit_price, " +
                "p.discount_percent, " +
                "MAX(CASE WHEN pi.main_image = 1 THEN pi.url ELSE NULL END) AS main_image_url, " +
                "GROUP_CONCAT(CASE WHEN pi.main_image = 0 THEN pi.url ELSE NULL END) AS other_image_urls " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "WHERE c.id = :categoryId " +
                "GROUP BY p.id " +
                "ORDER BY p.created_at DESC " +
                "LIMIT 5";

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql).bind("categoryId", categoryId);

            return query.map((rs, ctx) -> {
                // Lấy thông tin sản phẩm
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double unitPrice = rs.getDouble("unit_price");
                double discountPercent = rs.getDouble("discount_percent");

                // Lấy URL của hình chính
                String mainImageUrl = rs.getString("main_image_url");
                ProductImage mainImage = null;
                if (mainImageUrl != null) {
                    mainImage = new ProductImage(mainImageUrl, true);
                }

                // Lấy danh sách các hình ảnh khác
                String otherImageUrls = rs.getString("other_image_urls");
                List<ProductImage> productImages = new ArrayList<>();
                if (mainImage != null) {
                    productImages.add(mainImage); // Thêm hình chính vào đầu danh sách
                }
                if (otherImageUrls != null && !otherImageUrls.isEmpty()) {
                    String[] urls = otherImageUrls.split(",");
                    for (String url : urls) {
                        if (url != null && !url.isEmpty()) {
                            productImages.add(new ProductImage(url, false));
                        }
                    }
                }

                // Tạo đối tượng Product
                return new Product(
                        productId,
                        productName,
                        unitPrice,
                        discountPercent,
                        productImages
                );
            }).list();
        });
    }

    public List<Product> getProductsByCategory2(int categoryId) {
        String sql = "SELECT " +
                "p.id AS product_id, " +
                "p.product_name, " +
                "p.unit_price, " +
                "p.discount_percent, " +
                "MAX(CASE WHEN pi.main_image = 1 THEN pi.url ELSE NULL END) AS main_image_url, " +
                "GROUP_CONCAT(CASE WHEN pi.main_image = 0 THEN pi.url ELSE NULL END) AS other_image_urls " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "WHERE c.id = :categoryId " +
                "GROUP BY p.id " +
                "ORDER BY p.created_at DESC ";

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql).bind("categoryId", categoryId);

            return query.map((rs, ctx) -> {
                // Lấy thông tin sản phẩm
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double unitPrice = rs.getDouble("unit_price");
                double discountPercent = rs.getDouble("discount_percent");

                // Lấy URL của hình chính
                String mainImageUrl = rs.getString("main_image_url");
                ProductImage mainImage = null;
                if (mainImageUrl != null) {
                    mainImage = new ProductImage(mainImageUrl, true);
                }

                // Lấy danh sách các hình ảnh khác
                String otherImageUrls = rs.getString("other_image_urls");
                List<ProductImage> productImages = new ArrayList<>();
                if (mainImage != null) {
                    productImages.add(mainImage); // Thêm hình chính vào đầu danh sách
                }
                if (otherImageUrls != null && !otherImageUrls.isEmpty()) {
                    String[] urls = otherImageUrls.split(",");
                    for (String url : urls) {
                        if (url != null && !url.isEmpty()) {
                            productImages.add(new ProductImage(url, false));
                        }
                    }
                }

                // Tạo đối tượng Product
                return new Product(
                        productId,
                        productName,
                        unitPrice,
                        discountPercent,
                        productImages
                );
            }).list();
        });
    }

    // Lấy dssp cho trang product Admin
    public List<Product> getProductsForAdminPage() {
        String sql = "SELECT " +
                "p.id AS id, " +
                "p.discount_percent AS discountPercent, " +
                "p.product_name AS productName, " +
                "p.unit_price AS unitPrice, " +
                "p.created_at AS createdAt, " +
                "sc.name AS categoryName, " +
                "COALESCE(pi.url, 'https://via.placeholder.com/50') AS imageUrl " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.main_image = TRUE";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Product(
                                rs.getInt("id"),
                                rs.getString("imageUrl"),
                                rs.getString("productName"),
                                rs.getDouble("unitPrice"),
                                rs.getString("categoryName"),
                                rs.getDate("createdAt"),
                                rs.getDouble("discountPercent")
                        ))
                        .list()
        );
    }

    // Tìm kiếm sp theo tên, danh mục
    public List<Product> getProductsByKeyword(String keyword) {
        String sql = "SELECT " +
                "p.id AS id, " +
                "p.product_name AS productName, " +
                "p.unit_price AS unitPrice, " +
                "p.created_at AS createdAt, " +
                "sc.name AS categoryName, " +
                "COALESCE(pi.url, 'https://via.placeholder.com/50') AS imageUrl " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.main_image = TRUE " +
                "WHERE (:keyword IS NULL OR p.product_name LIKE :keyword OR sc.name LIKE :keyword)";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword != null ? "%" + keyword + "%" : null)
                        .map((rs, ctx) -> new Product(
                                rs.getInt("id"),
                                rs.getString("imageUrl"),
                                rs.getString("productName"),
                                rs.getDouble("unitPrice"),
                                rs.getString("categoryName"),
                                rs.getDate("createdAt")
                        ))
                        .list()
        );
    }

    // Lấy tổng số lượng sp
    public int getTotalProducts() {
        String sql = "SELECT SUM(stock_quantity) FROM products";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    //Tong danh muc
    public int getCategoryQuantity() {
        String sql = "select count(*) from categories";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    //So sp het hàng
    public int getOutOfStockProducts() {
        String sql = "SELECT COUNT(*) FROM products WHERE stock_quantity = 0";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    //Sp mới
    public int getNewProductsInLast7Days() {
        String sql = "SELECT COUNT(*) AS total FROM products WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    //Lay ra chi tiet sp
    public ProductDetail getProductDetailById(int id) {
        String sql = "SELECT p.id, p.subCategory_id, p.product_name, p.unit_price, p.created_at, p.stock_quantity, " +
                "p.product_status, p.rating, p.DESC_1 AS description, p.warranty_period, p.light_color, " +
                "p.material, p.voltage, p.usage_age, p.discount_percent, " +
                "c.category_name, pi.url AS main_image_url " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "JOIN categories c ON sc.category_id = c.id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.main_image = true " +
                "WHERE p.id = :id";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .map((rs, ctx) -> new ProductDetail(
                                rs.getInt("id"),
                                rs.getInt("subCategory_id"),
                                rs.getString("product_name"),
                                rs.getDouble("unit_price"),
                                rs.getDate("created_at"),
                                rs.getInt("stock_quantity"),
                                rs.getString("product_status"),
                                rs.getDouble("rating"),
                                rs.getString("description"),
                                rs.getString("warranty_period"),
                                rs.getString("light_color"),
                                rs.getString("material"),
                                rs.getString("voltage"),
                                rs.getString("usage_age"),
                                rs.getDouble("discount_percent"),
                                null, // Danh sách hình ảnh sẽ không cần nếu chỉ lấy hình chính
                                rs.getString("category_name"), // Tên danh mục
                                rs.getString("main_image_url") // Hình ảnh chính
                        ))
                        .findOne()
                        .orElse(null)
        );
    }

    //Lay id cate khi nhap ten
    public int getSubCategoryIdByName(String subCategoryName) {
        String sql = "SELECT id FROM sub_categories WHERE name = :subCategoryName";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("subCategoryName", subCategoryName)
                        .mapTo(Integer.class)
                        .findOne() // Tìm một kết quả duy nhất
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục con: " + subCategoryName))
        );
    }

    //Them sp
    public boolean addProduct(ProductDetail product, String subCategoryName) {
        // Lấy subCategoryId từ tên danh mục con
        int subCategoryId = getSubCategoryIdByName(subCategoryName);

        String productSql = "INSERT INTO products (subCategory_id, product_name, unit_price, created_at, stock_quantity, product_status, rating, DESC_1, warranty_period, light_color, material, voltage, usage_age, discount_percent) "
                + "VALUES (:subCategoryId, :productName, :unitPrice, :createdAt, :stockQuantity, :productStatus, :rating, :description, :warrantyPeriod, :lightColor, :material, :voltage, :usageAge, :discountPercent)";

        String imageSql = "INSERT INTO product_images (product_id, url, main_image) VALUES (:productId, :url, :mainImage)";

        return jdbi.inTransaction(handle -> {
            // Thêm sản phẩm vào bảng `products`
            int productId = handle.createUpdate(productSql)
                    .bind("subCategoryId", subCategoryId) // Gán subCategoryId lấy từ tên
                    .bind("productName", product.getProductName())
                    .bind("unitPrice", product.getUnitPrice())
                    .bind("createdAt", new java.sql.Date(product.getCreatedAt().getTime()))
                    .bind("stockQuantity", product.getStockQuantity())
                    .bind("productStatus", product.getProductStatus())
                    .bind("rating", product.getRating())
                    .bind("description", product.getDescription())
                    .bind("warrantyPeriod", product.getWarrantyPeriod())
                    .bind("lightColor", product.getLightColor())
                    .bind("material", product.getMaterial())
                    .bind("voltage", product.getVoltage())
                    .bind("usageAge", product.getUsageAge())
                    .bind("discountPercent", product.getDiscountPercent())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            // Thêm danh sách hình ảnh vào bảng `product_images`
            if (product.getListImages() != null && !product.getListImages().isEmpty()) {
                for (ProductImage image : product.getListImages()) {
                    handle.createUpdate(imageSql)
                            .bind("productId", productId)
                            .bind("url", image.getUrl())
                            .bind("mainImage", image.isMainImage())
                            .execute();
                }
            }

            return true;
        });
    }

    //Xoa sp
    public boolean deleteProductById(int productId) {
        String deleteProductImagesSql = "DELETE FROM product_images WHERE product_id = :productId";
        String deleteProductSql = "DELETE FROM products WHERE id = :productId";

        return jdbi.inTransaction(handle -> {
            // Xóa các hình ảnh liên quan đến sản phẩm trước
            handle.createUpdate(deleteProductImagesSql)
                    .bind("productId", productId)
                    .execute();

            // Xóa sản phẩm khỏi bảng `products`
            int rowsDeleted = handle.createUpdate(deleteProductSql)
                    .bind("productId", productId)
                    .execute();

            // Trả về true nếu xóa thành công (rowsDeleted > 0)
            return rowsDeleted > 0;
        });
    }

    public List<TopProduct> getTopSellingProducts() {
        String sql = "SELECT " +
                "p.product_name AS productName, " +
                "SUM(od.quantity) AS quantitySold, " +
                "SUM(od.quantity * od.unit_price) AS totalRevenue, " +
                "p.stock_quantity AS stockQuantity " +
                "FROM products p " +
                "JOIN order_details od ON p.id = od.product_id " +
                "JOIN orders o ON od.order_id = o.id " +
                "WHERE o.order_status = 'Pending' " +
                "GROUP BY p.id, p.product_name, p.stock_quantity " +
                "ORDER BY quantitySold DESC " +
                "LIMIT 5";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new TopProduct(
                                rs.getString("productName"),
                                rs.getInt("quantitySold"),
                                rs.getDouble("totalRevenue"),
                                rs.getInt("stockQuantity")
                        ))
                        .list()
        );
    }

    public List<Product> getProductsBySubCategory(int subCategoryId) {
        String sql = "SELECT p.id AS product_id, p.product_name, p.unit_price, " +
                "COALESCE(p.discount_percent, 0) AS discount_percent, " +
                "pi.url AS image_url, pi.main_image " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "WHERE p.subCategory_id = :subCategoryId " +
                "ORDER BY p.created_at DESC";

        return jdbi.withHandle(handle -> {
            Map<Integer, Product> productMap = new HashMap<>();
            handle.createQuery(sql)
                    .bind("subCategoryId", subCategoryId)
                    .map((rs, ctx) -> {
                        int productId = rs.getInt("product_id");
                        Product product = productMap.get(productId);
                        if (product == null) {
                            product = new Product(
                                    productId,
                                    rs.getString("product_name"),
                                    rs.getDouble("unit_price"),
                                    rs.getDouble("discount_percent"),
                                    new ArrayList<>()
                            );
                            productMap.put(productId, product);
                        }
                        String imageUrl = rs.getString("image_url");
                        if (imageUrl != null) {
                            product.getListImg().add(new ProductImage(
                                    imageUrl,
                                    rs.getBoolean("main_image")
                            ));
                        }
                        return product;
                    }).list();

            return new ArrayList<>(productMap.values());
        });
    }


//    public boolean editProductDetail(ProductDetail productDetail) {
//        return jdbi.inTransaction(handle -> {
//            // Cập nhật bảng `products`
//            String productSql = "UPDATE products SET " +
//                    "product_name = :productName, " +
//                    "unit_price = :unitPrice, " +
//                    "stock_quantity = :stockQuantity, " +
//                    "product_status = :productStatus, " +
//                    "rating = :rating, " +
//                    "DESC_1 = :description, " +
//                    "warranty_period = :warrantyPeriod, " +
//                    "light_color = :lightColor, " +
//                    "material = :material, " +
//                    "voltage = :voltage, " +
//                    "usage_age = :usageAge, " +
//                    "discount_percent = :discountPercent, " +
//                    "subCategory_id = :subCategoryId " +
//                    "WHERE id = :id";
//
//            int updatedProduct = handle.createUpdate(productSql)
//                    .bind("id", productDetail.getId())
//                    .bind("productName", productDetail.getProductName())
//                    .bind("unitPrice", productDetail.getUnitPrice())
//                    .bind("stockQuantity", productDetail.getStockQuantity())
//                    .bind("productStatus", productDetail.getProductStatus())
//                    .bind("rating", productDetail.getRating())
//                    .bind("description", productDetail.getDescription())
//                    .bind("warrantyPeriod", productDetail.getWarrantyPeriod())
//                    .bind("lightColor", productDetail.getLightColor())
//                    .bind("material", productDetail.getMaterial())
//                    .bind("voltage", productDetail.getVoltage())
//                    .bind("usageAge", productDetail.getUsageAge())
//                    .bind("discountPercent", productDetail.getDiscountPercent())
//                    .bind("subCategoryId", productDetail.getSubCategoryId())
//                    .execute();
//
//            // Cập nhật bảng `product_images`
//            String imageSql = "UPDATE product_images SET url = :mainImageUrl " +
//                    "WHERE product_id = :id AND main_image = true";
//
//            int updatedImage = handle.createUpdate(imageSql)
//                    .bind("id", productDetail.getId())
//                    .bind("mainImageUrl", productDetail.getMainImageUrl())
//                    .execute();
//
//            return updatedProduct > 0 && updatedImage > 0;
//        });
//    }

    public boolean editProductDetail(ProductDetail productDetail) {
        return jdbi.inTransaction(handle -> {
            // Cập nhật bảng `products`
            String productSql = "UPDATE products SET " +
                    "product_name = :productName, " +
                    "unit_price = :unitPrice, " +
                    "stock_quantity = :stockQuantity, " +
                    "product_status = :productStatus, " +
                    "rating = :rating, " +
                    "DESC_1 = :description, " +
                    "warranty_period = :warrantyPeriod, " +
                    "light_color = :lightColor, " +
                    "material = :material, " +
                    "voltage = :voltage, " +
                    "usage_age = :usageAge, " +
                    "discount_percent = :discountPercent, " +
                    "subCategory_id = :subCategoryId " +
                    "WHERE id = :id";

            int updatedProduct = handle.createUpdate(productSql)
                    .bind("id", productDetail.getId())
                    .bind("productName", productDetail.getProductName())
                    .bind("unitPrice", productDetail.getUnitPrice())
                    .bind("stockQuantity", productDetail.getStockQuantity())
                    .bind("productStatus", productDetail.getProductStatus())
                    .bind("rating", productDetail.getRating())
                    .bind("description", productDetail.getDescription())
                    .bind("warrantyPeriod", productDetail.getWarrantyPeriod())
                    .bind("lightColor", productDetail.getLightColor())
                    .bind("material", productDetail.getMaterial())
                    .bind("voltage", productDetail.getVoltage())
                    .bind("usageAge", productDetail.getUsageAge())
                    .bind("discountPercent", productDetail.getDiscountPercent())
                    .bind("subCategoryId", productDetail.getSubCategoryId())
                    .execute();

            // Xóa ảnh cũ
            String deleteImageSql = "DELETE FROM product_images WHERE product_id = :id";
            handle.createUpdate(deleteImageSql)
                    .bind("id", productDetail.getId())
                    .execute();

            // Thêm ảnh mới
            String insertImageSql = "INSERT INTO product_images (product_id, url, main_image) VALUES (:productId, :url, :mainImage)";
            for (ProductImage image : productDetail.getListImages()) {
                handle.createUpdate(insertImageSql)
                        .bind("productId", productDetail.getId())
                        .bind("url", image.getUrl())
                        .bind("mainImage", image.isMainImage())
                        .execute();
            }

            return updatedProduct > 0;
        });
    }


    public Product getProductById(int id) {
        String sql = "SELECT p.id AS product_id, p.product_name, p.unit_price, p.discount_percent, " +
                "pi.url AS image_url, pi.main_image " +
                "FROM products p " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.main_image = TRUE " + // Lấy hình ảnh chính
                "WHERE p.id = :id";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .map((rs, ctx) -> {
                            // Tạo đối tượng Product từ kết quả
                            Product product = new Product(
                                    rs.getInt("product_id"),
                                    rs.getString("product_name"),
                                    rs.getDouble("unit_price"),
                                    rs.getDouble("discount_percent"),
                                    new ArrayList<>()
                            );

                            // Thêm hình ảnh chính nếu có
                            String imageUrl = rs.getString("image_url");
                            if (imageUrl != null) {
                                product.getListImg().add(new ProductImage(imageUrl, true));
                            }

                            return product;
                        })
                        .findOne() // Chỉ cần lấy một kết quả
                        .orElse(null) // Trả về null nếu không tìm thấy sản phẩm
        );
    }

    public boolean isProductInStock(int productId) {
        String query = "SELECT stock_quantity > 0 FROM products WHERE id = :productId";

        // Thực hiện truy vấn và trả về kết quả boolean
        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind("productId", productId)
                        .mapTo(Boolean.class)
                        .findOne()
                        .orElse(false) // Trả về false nếu không có bản ghi nào
        );
    }

    // lấy ds sap theo trang
    public List<Product> getProductsByPage(int page, int pageSize) {
        String sql = "SELECT p.id AS product_id, p.product_name, p.unit_price, p.discount_percent, " +
                "pi.url AS image_url, pi.main_image " +
                "FROM products p " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "ORDER BY p.created_at DESC " +
                "LIMIT :limit OFFSET :offset";

        int offset = (page - 1) * pageSize;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .map((rs, ctx) -> {
                            Product product = new Product(
                                    rs.getInt("product_id"),
                                    rs.getString("product_name"),
                                    rs.getDouble("unit_price"),
                                    rs.getDouble("discount_percent"),
                                    new ArrayList<>()
                            );

                            String imageUrl = rs.getString("image_url");
                            if (imageUrl != null) {
                                product.getListImg().add(new ProductImage(
                                        imageUrl,
                                        rs.getBoolean("main_image")
                                ));
                            }
                            return product;
                        }).list()
        );
    }


    public List<Product> searchProductsByName(String productName) {
        String sql = "SELECT p.id AS product_id, p.product_name, p.unit_price, p.discount_percent, " +
                "pi.url AS image_url, pi.main_image " +
                "FROM products p " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "WHERE p.product_name LIKE :productName " + // Tìm kiếm theo tên sản phẩm
                "ORDER BY p.created_at DESC ";

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql)
                    .bind("productName", "%" + productName + "%"); // Sử dụng LIKE với ký tự đại diện (%)

            Map<Integer, Product> productMap = new HashMap<>();

            query.map((rs, ctx) -> {
                int productId = rs.getInt("product_id");
                Product product = productMap.get(productId);

                if (product == null) {
                    product = new Product(
                            productId,
                            rs.getString("product_name"),
                            rs.getDouble("unit_price"),
                            rs.getDouble("discount_percent"),
                            new ArrayList<>()
                    );
                    productMap.put(productId, product);
                }

                String imageUrl = rs.getString("image_url");
                if (imageUrl != null) {
                    product.getListImg().add(new ProductImage(
                            imageUrl,
                            rs.getBoolean("main_image")
                    ));
                }

                return product;
            }).list();

            return new ArrayList<>(productMap.values());
        });
    }

    // Trừ số lượng sản phẩm trong kho
    public void decreaseStockQuantity(int productId, int quantity) {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - :quantity " +
                "WHERE id = :productId AND stock_quantity >= :quantity";

        jdbi.useHandle(handle -> {
            int rowsAffected = handle.createUpdate(sql)
                    .bind("quantity", quantity)
                    .bind("productId", productId)
                    .execute();

            if (rowsAffected == 0) {
                throw new RuntimeException("Không đủ hàng tồn kho cho sản phẩm ID: " + productId);
            }
        });
    }




    // Lấy danh sách sản phẩm theo chương trình khuyến mãi
    public List<Product> getProductsByPromotion(int promotionId) {
        String sql = "SELECT " +
                "p.id AS product_id, " +
                "p.product_name, " +
                "p.unit_price, " +
                "p.discount_percent, " +
                "pi.url AS image_url, " +
                "pi.main_image " +
                "FROM products p " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "JOIN promotion_programs pp ON p.id = pp.product_id " +
                "JOIN promotions pr ON pp.promotion_id = pr.id " +
                "WHERE pr.id = :promotionId " +
                "ORDER BY p.created_at DESC";

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql).bind("promotionId", promotionId);
            Map<Integer, Product> productMap = new HashMap<>();

            query.map((rs, ctx) -> {
                int productId = rs.getInt("product_id");
                Product product = productMap.get(productId);

                if (product == null) {
                    product = new Product(
                            productId,
                            rs.getString("product_name"),
                            rs.getDouble("unit_price"),
                            rs.getDouble("discount_percent"),
                            new ArrayList<>()
                    );
                    productMap.put(productId, product);
                }

                String imageUrl = rs.getString("image_url");
                if (imageUrl != null) {
                    product.getListImg().add(new ProductImage(
                            imageUrl,
                            rs.getBoolean("main_image")
                    ));
                }

                return product;
            }).list();

            return new ArrayList<>(productMap.values());
        });
    }

    //ProductViewDetail
    public int getSoldQuantity(int productId) {
        String sql = "SELECT SUM(quantity) FROM order_details WHERE product_id = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, productId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0)
        );
    }

    public int getStockQuantity(int productId) {
        String sql = "SELECT stock_quantity FROM products WHERE id = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, productId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0)
        );
    }

    public double getProductRating(int productId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE product_id = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, productId)
                        .mapTo(Double.class)
                        .findOne()
                        .orElse(0.0)
        );
    }

    public int getReviewsCount(int productId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE product_id = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind(0, productId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0)
        );
    }

    public List<String> getAllProductUrls(int productId) {
        String sql = "SELECT pi.url " +
                "FROM product_images pi " +
                "JOIN products p ON pi.product_id = p.id " +
                "WHERE pi.url IS NOT NULL AND pi.product_id = :productId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId) // Gắn giá trị productId vào query
                        .mapTo(String.class) // Map kết quả trực tiếp sang danh sách String
                        .list()
        );
    }

    //SP bán chạy
    public List<Product> getBestSellingProducts() {
        String sql = "SELECT " +
                "p.id AS product_id, " +
                "p.product_name, " +
                "p.unit_price, " +
                "p.discount_percent, " +
                "MAX(CASE WHEN pi.main_image = 1 THEN pi.url ELSE NULL END) AS main_image_url, " +
                "GROUP_CONCAT(pi.url) AS all_images, " +
                "SUM(od.quantity) AS total_sold " +
                "FROM products p " +
                "JOIN order_details od ON p.id = od.product_id " +
                "LEFT JOIN product_images pi ON p.id = pi.product_id " +
                "GROUP BY p.id " +
                "ORDER BY total_sold DESC";

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql);

            return query.map((rs, ctx) -> {
                // Lấy thông tin sản phẩm
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double unitPrice = rs.getDouble("unit_price");
                double discountPercent = rs.getDouble("discount_percent");

                // Lấy URL của hình ảnh chính
                String mainImageUrl = rs.getString("main_image_url");

                // Lấy danh sách URL của tất cả hình ảnh
                String allImages = rs.getString("all_images");
                List<ProductImage> productImages = new ArrayList<>();

                // Xử lý hình ảnh chính
                if (mainImageUrl != null) {
                    productImages.add(new ProductImage(mainImageUrl, true));
                }

                // Xử lý hình ảnh phụ
                if (allImages != null && !allImages.isEmpty()) {
                    String[] urls = allImages.split(",");
                    for (String url : urls) {
                        if (url != null && !url.equals(mainImageUrl)) { // Loại trừ hình ảnh chính
                            productImages.add(new ProductImage(url.trim(), false));
                        }
                    }
                }

                // Tạo đối tượng Product
                return new Product(
                        productId,
                        productName,
                        unitPrice,
                        discountPercent,
                        productImages // Danh sách ProductImage
                );
            }).list();
        });
    }

    public String getCategoryNameByProductId(int productId) {
        String sql = "SELECT " +
                "c.category_name, " +
                "sc.name AS sub_category_name " +
                "FROM products p " +
                "JOIN sub_categories sc ON p.subCategory_id = sc.id " +
                "JOIN categories c ON sc.category_id = c.id " +
                "WHERE p.id = :productId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .map((rs, ctx) -> rs.getString("category_name") + " › " + rs.getString("sub_category_name"))
                        .one()
        );
    }


    public List<Product> getRelatedProducts(int productId) {
        String sql = """
        SELECT 
            p.id AS product_id, 
            p.product_name, 
            p.unit_price, 
            p.discount_percent, 
            MAX(CASE WHEN pi.main_image = 1 THEN pi.url ELSE NULL END) AS main_image_url, 
            GROUP_CONCAT(pi.url) AS all_images 
        FROM products p
        LEFT JOIN product_images pi ON p.id = pi.product_id
        WHERE p.subCategory_id = (
            SELECT subCategory_id 
            FROM products 
            WHERE id = :productId
        ) 
        AND p.id != :productId
        GROUP BY p.id
        ORDER BY p.id
        LIMIT 4
    """;

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql).bind("productId", productId);

            return query.map((rs, ctx) -> {
                // Lấy thông tin sản phẩm
                int productIdFromDb = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double unitPrice = rs.getDouble("unit_price");
                double discountPercent = rs.getDouble("discount_percent");

                // Lấy URL của hình ảnh chính
                String mainImageUrl = rs.getString("main_image_url");

                // Lấy danh sách URL của tất cả hình ảnh
                String allImages = rs.getString("all_images");
                List<ProductImage> productImages = new ArrayList<>();

                // Xử lý hình ảnh chính
                if (mainImageUrl != null) {
                    productImages.add(new ProductImage(mainImageUrl, true));
                }

                // Xử lý hình ảnh phụ
                if (allImages != null && !allImages.isEmpty()) {
                    String[] urls = allImages.split(",");
                    for (String url : urls) {
                        if (url != null && !url.equals(mainImageUrl)) { // Loại trừ hình ảnh chính
                            productImages.add(new ProductImage(url.trim(), false));
                        }
                    }
                }

                // Tạo đối tượng Product
                return new Product(
                        productIdFromDb,
                        productName,
                        unitPrice,
                        discountPercent,
                        productImages // Danh sách ProductImage
                );
            }).list();
        });
    }


    public static void main(String[] args) {
        // Khởi tạo dịch vụ sản phẩm
        ProductDao productServices = new ProductDao();

        // Gọi phương thức getBestSellingProducts()
        List<Product> bestSellingProducts = productServices.getBestSellingProducts();

        // In kết quả ra console để kiểm tra
        System.out.println("Sản phẩm bán chạy:");
        for (Product product : bestSellingProducts) {
            System.out.println("ID: " + product.getId());
            System.out.println("Tên: " + product.getProductName());
            System.out.println("Giá: " + product.getUnitPrice());
            System.out.println("Giảm giá: " + product.getDiscountPercent() + "%");
            System.out.println("Hình ảnh chính: " + product.getImageUrl());

            System.out.println("Danh sách hình ảnh:");
            for (ProductImage image : product.getListImg()) {
                System.out.println("  - " + image.getUrl() + (image.isMainImage() ? " (Chính)" : ""));
            }
            System.out.println("-----");
        }
    }
}