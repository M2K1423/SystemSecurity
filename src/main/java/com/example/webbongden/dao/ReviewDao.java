package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Review;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ReviewDao {
    private final Jdbi jdbi;

    public ReviewDao() {
        this.jdbi = JDBIConnect.get();
    }

    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (product_id, account_id, content, rating, review_typpe) VALUES (:productId, :accountId, :content, :rating, :reviewType)";
        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("productId", review.getProductId())
                        .bind("accountId", review.getAccountId())
                        .bind("content", review.getContent())
                        .bind("rating", review.getRating())
                        .bind("reviewType", "product_review")
                        .execute() > 0
        );
    }

    public List<Review> getReviewsByProductId(int productId) {
        String sql = "SELECT * FROM reviews WHERE product_id = :productId";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("productId", productId)
                        .mapToBean(Review.class)
                        .list()
        );
    }
}
