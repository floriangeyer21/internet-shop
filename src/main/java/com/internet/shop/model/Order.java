package com.internet.shop.model;

import java.util.List;

public class Order {
    private Long id;
    private List<Product> products;
    private Long userId;
    private Double price;

    public Order(Long userId, List<Product> products) {
        this.userId = userId;
        this.products = products;
    }

    public Double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{"
                + "id=" + id
                + ", products=" + products
                + ", userId="
                + userId
                + '}';
    }
}
