package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.PageDTO;
import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.model.PageResult;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListProductsUseCase")
class ListProductsUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper;
    private ListProductsUseCase useCase;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        useCase = new ListProductsUseCase(productRepository, productMapper);
    }

    @Test
    @DisplayName("Deve retornar página de produtos")
    void shouldReturnPageOfProducts() {
        Instant now = Instant.now();
        Product product = new Product(UUID.randomUUID(), "Notebook", "Desc", new BigDecimal("2999.90"), 10, true, now, now, 0L);
        PageResult<Product> pageResult = new PageResult<>(List.of(product), 0, 20, 1, 1);

        when(productRepository.findAll(anyInt(), anyInt())).thenReturn(pageResult);

        PageDTO<ProductDTO> result = useCase.execute(0, 20);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Notebook", result.content().get(0).name());
        assertTrue(result.first());
        assertTrue(result.last());
    }

    @Test
    @DisplayName("Deve usar tamanho padrão quando inválido")
    void shouldUseDefaultSizeWhenInvalid() {
        PageResult<Product> pageResult = new PageResult<>(List.of(), 0, 20, 0, 0);
        when(productRepository.findAll(0, 20)).thenReturn(pageResult);

        PageDTO<ProductDTO> result = useCase.execute(0, -1);

        assertNotNull(result);
        assertEquals(20, result.size());
    }

    @Test
    @DisplayName("Deve limitar tamanho máximo")
    void shouldLimitMaxSize() {
        PageResult<Product> pageResult = new PageResult<>(List.of(), 0, 100, 0, 0);
        when(productRepository.findAll(0, 100)).thenReturn(pageResult);

        PageDTO<ProductDTO> result = useCase.execute(0, 500);

        assertNotNull(result);
        assertEquals(100, result.size());
    }
}
