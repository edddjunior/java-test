package com.inventory.stock.domain.gateway;

import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {

    Optional<ProductStock> getProduct(UUID productId);

    record ProductStock(UUID id, String name, Integer stockQuantity) {}
}
