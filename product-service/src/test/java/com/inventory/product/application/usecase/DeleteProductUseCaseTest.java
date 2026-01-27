package com.inventory.product.application.usecase;

import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteProductUseCase")
class DeleteProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private DeleteProductUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteProductUseCase(productRepository);
    }

    @Test
    @DisplayName("Deve deletar produto quando existe")
    void shouldDeleteProductWhenExists() {
        UUID id = UUID.randomUUID();
        when(productRepository.existsById(id)).thenReturn(true);

        useCase.execute(id);

        verify(productRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não existe")
    void shouldThrowExceptionWhenNotExists() {
        UUID id = UUID.randomUUID();
        when(productRepository.existsById(id)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(id));

        verify(productRepository, never()).deleteById(any());
    }
}
