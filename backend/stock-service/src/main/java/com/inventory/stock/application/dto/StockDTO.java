package com.inventory.stock.application.dto;

import java.util.UUID;

public record StockDTO(
        UUID productId,
        String productName,
        Integer quantity
) {}
