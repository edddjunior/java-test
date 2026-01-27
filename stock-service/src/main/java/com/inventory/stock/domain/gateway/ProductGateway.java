package com.inventory.stock.domain.gateway;

import com.inventory.stock.domain.model.ProductStock;

import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {

    Optional<ProductStock> getProduct(UUID productId);
}
