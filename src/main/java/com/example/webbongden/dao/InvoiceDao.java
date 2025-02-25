package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Invoices;
import com.example.webbongden.dao.model.OrderDetail;
import org.jdbi.v3.core.Jdbi;

import java.util.Date;
import java.util.List;

public class InvoiceDao {
    private final Jdbi jdbi;

    // Constructor truyền Jdbi
    public InvoiceDao() {
        this.jdbi = JDBIConnect.get();
    }
//    String sql = "INSERT INTO invoices (promotion_id, account_id, created_at, total_price, payment_status) " +
//            "VALUES (:promotionId, :accountId, :createdAt, :totalPrice, :paymentStatus)";
    // Hàm tạo hóa đơn
    public int createInvoice(Invoices invoice) {
        String sql = "INSERT INTO invoices (promotion_id, account_id, created_at, total_price, payment_status) " +
                "VALUES (:promotionId, :accountId, :createdAt, :totalPrice, :paymentStatus)";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("promotionId", invoice.getPromotionId() == 0 ? null : invoice.getPromotionId()) // Đưa NULL nếu promotionId là 0
                        .bind("accountId", invoice.getAccountId())
                        .bind("createdAt", new java.sql.Date(invoice.getCreatedAt().getTime()))
                        .bind("totalPrice", invoice.getTotalPrice())
                        .bind("paymentStatus", invoice.getPaymentStatus())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one()
        );
    }

    // Hàm tạo chi tiết hóa đơn
    public void createInvoiceDetails(int invoiceId, List<OrderDetail> orderDetails) {
        String sql = "INSERT INTO invoice_details (invoice_id, product_id, unit_price, quantity, item_discount, amount) " +
                "VALUES (:invoiceId, :productId, :unitPrice, :quantity, :itemDiscount, :amount)";

        jdbi.useHandle(handle -> {
            for (OrderDetail detail : orderDetails) {
                handle.createUpdate(sql)
                        .bind("invoiceId", invoiceId)
                        .bind("productId", detail.getProductId())
                        .bind("unitPrice", detail.getUnitPrice())
                        .bind("quantity", detail.getQuantity())
                        .bind("itemDiscount", detail.getItemDiscount())
                        .bind("amount", detail.getAmount())
                        .execute();
            }
        });
    }
}
