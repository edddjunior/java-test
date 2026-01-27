package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetProductUseCase")
class GetProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper;
    private GetProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        useCase = new GetProductUseCase(productRepository, productMapper);
    }

    @Test
    @DisplayName("Deve retornar produto quando encontrado")
    void shouldReturnProductWhenFound() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Product product = new Product(id, "Notebook", "Desc", new BigDecimal("2999.90"), 10, true, now, now, 0L);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductDTO result = useCase.execute(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals("Notebook", result.name());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não encontrado")
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(id));
    }
}
