package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductUseCase")
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper;
    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        useCase = new CreateProductUseCase(productRepository, productMapper);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void shouldCreateProductSuccessfully() {
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDTO result = useCase.execute("Notebook", "Descrição", new BigDecimal("2999.90"), 10);

        assertNotNull(result);
        assertEquals("Notebook", result.name());
        assertEquals("Descrição", result.description());
        assertEquals(new BigDecimal("2999.90"), result.price());
        assertEquals(10, result.stockQuantity());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar exceção para dados inválidos")
    void shouldThrowExceptionForInvalidData() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute("", "Desc", BigDecimal.TEN, 10));

        verify(productRepository, never()).save(any());
    }
}
