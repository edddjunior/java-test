package com.inventory.stock.application.usecase;

import com.inventory.stock.domain.gateway.ProductGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CheckStockUseCase {

    private static final int LOW_STOCK_THRESHOLD = 10;

    private final ProductGateway productGateway;

    public CheckStockUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public Optional<StockCheckResult> execute(UUID productId) {
        return productGateway.getProduct(productId)
                .map(product -> new StockCheckResult(
                        product.id(),
                        product.name(),
                        product.stockQuantity(),
                        product.stockQuantity() < LOW_STOCK_THRESHOLD
                ));
    }

    public record StockCheckResult(UUID productId, String productName, Integer stockQuantity, boolean lowStock) {}
}
