package com.inventory.product.application.mapper;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductMapper")
class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
    }

    @Test
    @DisplayName("Deve converter Product para ProductDTO corretamente")
    void shouldMapProductToDTO() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Product product = new Product(
                id, "Notebook", "Descrição", new BigDecimal("2999.90"),
                10, true, now, now, 1L
        );

        ProductDTO dto = mapper.toDTO(product);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        assertEquals("Notebook", dto.name());
        assertEquals("Descrição", dto.description());
        assertEquals(new BigDecimal("2999.90"), dto.price());
        assertEquals(10, dto.stockQuantity());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }

    @Test
    @DisplayName("Deve mapear produto com descrição nula")
    void shouldMapProductWithNullDescription() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Product product = new Product(
                id, "Mouse", null, BigDecimal.TEN,
                50, true, now, now, 0L
        );

        ProductDTO dto = mapper.toDTO(product);

        assertNotNull(dto);
        assertNull(dto.description());
    }
}
