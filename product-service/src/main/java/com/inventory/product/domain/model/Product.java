package com.inventory.product.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;

    public Product(UUID id, String name, String description, BigDecimal price, Integer stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static Product create(String name, String description, BigDecimal price, Integer stockQuantity) {
        validateName(name);
        validatePrice(price);
        validateStockQuantity(stockQuantity);
        return new Product(null, name.trim(), description, price, stockQuantity);
    }

    public void update(String name, String description, BigDecimal price, Integer stockQuantity) {
        validateName(name);
        validatePrice(price);
        validateStockQuantity(stockQuantity);
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Nome não pode exceder 255 caracteres");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Preço é obrigatório");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
    }

    private static void validateStockQuantity(Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantidade em estoque é obrigatória");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStockQuantity() { return stockQuantity; }
}
