package com.inventory.stock.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {

    @GetMapping("/api/v1/products/{id}")
    ProductResponse getProduct(@PathVariable("id") UUID productId);

    record ProductResponse(ProductData data) {
        record ProductData(UUID id, String name, BigDecimal price, Integer stockQuantity) {}
    }
}
