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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProductUseCase")
class UpdateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper;
    private UpdateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        useCase = new UpdateProductUseCase(productRepository, productMapper);
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void shouldUpdateProductSuccessfully() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Product product = new Product(id, "Original", "Desc", BigDecimal.TEN, 5, true, now, now, 0L);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductDTO result = useCase.execute(id, "Atualizado", "Nova Desc", new BigDecimal("99.90"), 20);

        assertNotNull(result);
        assertEquals("Atualizado", result.name());
        assertEquals(new BigDecimal("99.90"), result.price());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não encontrado")
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                useCase.execute(id, "Nome", "Desc", BigDecimal.TEN, 10));

        verify(productRepository, never()).save(any());
    }
}
