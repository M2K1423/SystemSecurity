package com.example.webbongden.dao;

import com.example.webbongden.dao.db.JDBIConnect;
import com.example.webbongden.dao.model.Shipping;
import org.jdbi.v3.core.Jdbi;

public class ShippingDao {
    private final Jdbi jdbi;

    public ShippingDao() {
        this.jdbi = JDBIConnect.get();
    }

    public void insertShipping(Shipping shipping) {
        String sql = "INSERT INTO shipping (order_id, pickup_date, shipping_status, address, carrier) " +
                "VALUES (:orderId, :pickupDate, :shippingStatus, :address, :carrier)";

        jdbi.useHandle(handle -> handle.createUpdate(sql)
                .bind("orderId", shipping.getOrderId())
                .bind("pickupDate", new java.sql.Date(shipping.getPickupDate().getTime()))
                .bind("shippingStatus", shipping.getShippingStatus())
                .bind("address", shipping.getAddress())
                .bind("carrier", shipping.getCarrier())
                .execute());
    }
}
