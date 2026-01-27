package com.inventory.stock.application.mapper;

import com.inventory.stock.application.dto.StockDTO;
import com.inventory.stock.domain.model.ProductStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StockMapper")
class StockMapperTest {

    private StockMapper stockMapper;

    @BeforeEach
    void setUp() {
        stockMapper = new StockMapper();
    }

    @Test
    @DisplayName("Deve mapear ProductStock para StockDTO corretamente")
    void shouldMapProductStockToStockDTO() {
        UUID id = UUID.randomUUID();
        ProductStock productStock = new ProductStock(id, "Notebook", 50);

        StockDTO result = stockMapper.toDTO(productStock);

        assertEquals(id, result.productId());
        assertEquals("Notebook", result.productName());
        assertEquals(50, result.quantity());
    }

    @Test
    @DisplayName("Deve mapear estoque zero corretamente")
    void shouldMapZeroStock() {
        UUID id = UUID.randomUUID();
        ProductStock productStock = new ProductStock(id, "Mouse", 0);

        StockDTO result = stockMapper.toDTO(productStock);

        assertEquals(0, result.quantity());
    }
}
