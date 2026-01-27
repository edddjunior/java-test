package com.inventory.stock.application.mapper;

import com.inventory.stock.application.dto.StockDTO;
import com.inventory.stock.domain.model.ProductStock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public StockDTO toDTO(ProductStock productStock) {
        return new StockDTO(
                productStock.id(),
                productStock.name(),
                productStock.stockQuantity()
        );
    }
}
