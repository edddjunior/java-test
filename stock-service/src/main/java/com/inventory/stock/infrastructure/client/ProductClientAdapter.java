package com.inventory.stock.infrastructure.client;

import com.inventory.stock.domain.gateway.ProductGateway;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductClientAdapter implements ProductGateway {

    private final ProductServiceClient productServiceClient;

    public ProductClientAdapter(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @Override
    public Optional<ProductStock> getProduct(UUID productId) {
        try {
            var response = productServiceClient.getProduct(productId);
            if (response != null && response.data() != null) {
                var data = response.data();
                return Optional.of(new ProductStock(data.id(), data.name(), data.stockQuantity()));
            }
            return Optional.empty();
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }
}
