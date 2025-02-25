package com.example.webbongden.dao.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<CartItem>();
    }

    public void addItem(CartItem item) {
        for (CartItem existingItem : cartItems) {
            if (existingItem.getProductId() == item.getProductId()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }


    public void removeItem(int productId) {
        cartItems.removeIf(item -> item.getProductId() == productId);
    }

    public void updateQuantity(int productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProductId() == productId) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    // Lấy tổng tiền tu gio hàng
    public String getTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getAmount();
        }
        DecimalFormat formatter = new DecimalFormat("#,###"); // Định dạng kiểu số tiền
        return formatter.format(totalPrice);
    }

    public double getTotalPriceNumber() {
        double totalPrice = 0.0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getAmount();
        }
        return totalPrice;
    }

    public List<CartItem> getItems() {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    public int getTotalQuantity() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
