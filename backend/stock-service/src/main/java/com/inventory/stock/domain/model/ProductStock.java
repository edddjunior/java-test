package com.inventory.stock.domain.model;

import java.util.UUID;

public record ProductStock(
        UUID id,
        String name,
        Integer stockQuantity
) {}
