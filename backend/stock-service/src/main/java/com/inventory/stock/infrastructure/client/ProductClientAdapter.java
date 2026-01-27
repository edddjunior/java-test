package com.inventory.stock.infrastructure.client;

import com.inventory.stock.infrastructure.exception.ServiceUnavailableException;
import com.inventory.stock.domain.gateway.ProductGateway;
import com.inventory.stock.domain.model.ProductStock;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductClientAdapter implements ProductGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductClientAdapter.class);

    private final ProductServiceClient productServiceClient;

    public ProductClientAdapter(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallback")
    public Optional<ProductStock> getProduct(UUID productId) {
        try {
            var response = productServiceClient.getProduct(productId);
            if (response != null && response.data() != null) {
                var data = response.data();
                return Optional.of(new ProductStock(data.id(), data.name(), data.stockQuantity()));
            }
            return Optional.empty();
        } catch (FeignException.NotFound e) {
            log.debug("Produto não encontrado: {}", productId);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    private Optional<ProductStock> fallback(UUID productId, Throwable t) {
        log.error("Circuit breaker ativado para produto {}: {}", productId, t.getMessage());
        throw new ServiceUnavailableException("Serviço de produtos indisponível");
    }
}
