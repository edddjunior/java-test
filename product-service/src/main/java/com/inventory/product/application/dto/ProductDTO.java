package com.inventory.product.application.dto;

import com.inventory.product.domain.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProductDTO from(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
