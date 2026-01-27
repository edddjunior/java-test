package com.inventory.stock.application.usecase;

import com.inventory.stock.application.dto.StockDTO;
import com.inventory.stock.application.mapper.StockMapper;
import com.inventory.stock.domain.gateway.ProductGateway;
import com.inventory.stock.domain.model.ProductStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckStockUseCase")
class CheckStockUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    private StockMapper stockMapper;
    private CheckStockUseCase useCase;

    @BeforeEach
    void setUp() {
        stockMapper = new StockMapper();
        useCase = new CheckStockUseCase(productGateway, stockMapper);
    }

    @Test
    @DisplayName("Deve retornar estoque quando produto encontrado")
    void shouldReturnStockWhenProductFound() {
        UUID productId = UUID.randomUUID();
        ProductStock product = new ProductStock(productId, "Notebook", 50);
        when(productGateway.getProduct(productId)).thenReturn(Optional.of(product));

        Optional<StockDTO> result = useCase.execute(productId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().productId());
        assertEquals("Notebook", result.get().productName());
        assertEquals(50, result.get().quantity());
    }

    @Test
    @DisplayName("Deve retornar vazio quando produto não encontrado")
    void shouldReturnEmptyWhenProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(productGateway.getProduct(productId)).thenReturn(Optional.empty());

        Optional<StockDTO> result = useCase.execute(productId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar estoque zero quando produto sem estoque")
    void shouldReturnZeroStock() {
        UUID productId = UUID.randomUUID();
        ProductStock product = new ProductStock(productId, "Mouse", 0);
        when(productGateway.getProduct(productId)).thenReturn(Optional.of(product));

        Optional<StockDTO> result = useCase.execute(productId);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().quantity());
    }
}
