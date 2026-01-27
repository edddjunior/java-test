package com.inventory.stock.application.usecase;

import com.inventory.stock.application.dto.StockDTO;
import com.inventory.stock.application.mapper.StockMapper;
import com.inventory.stock.domain.gateway.ProductGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CheckStockUseCase {

    private final ProductGateway productGateway;
    private final StockMapper stockMapper;

    public CheckStockUseCase(ProductGateway productGateway, StockMapper stockMapper) {
        this.productGateway = productGateway;
        this.stockMapper = stockMapper;
    }

    public Optional<StockDTO> execute(UUID productId) {
        return productGateway.getProduct(productId)
                .map(stockMapper::toDTO);
    }
}
