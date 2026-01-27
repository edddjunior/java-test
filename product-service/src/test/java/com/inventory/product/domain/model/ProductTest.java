package com.inventory.product.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product")
class ProductTest {

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Deve criar produto com dados válidos")
        void shouldCreateProductWithValidData() {
            Product product = Product.create("Notebook", "Descrição", new BigDecimal("2999.90"), 10);

            assertNotNull(product);
            assertEquals("Notebook", product.getName());
            assertEquals("Descrição", product.getDescription());
            assertEquals(new BigDecimal("2999.90"), product.getPrice());
            assertEquals(10, product.getStockQuantity());
            assertTrue(product.isActive());
            assertNotNull(product.getCreatedAt());
            assertNotNull(product.getUpdatedAt());
        }

        @Test
        @DisplayName("Deve lançar exceção para nome nulo")
        void shouldThrowExceptionForNullName() {
            assertThrows(IllegalArgumentException.class, () ->
                    Product.create(null, "Desc", BigDecimal.TEN, 10));
        }

        @Test
        @DisplayName("Deve lançar exceção para nome vazio")
        void shouldThrowExceptionForBlankName() {
            assertThrows(IllegalArgumentException.class, () ->
                    Product.create("   ", "Desc", BigDecimal.TEN, 10));
        }

        @Test
        @DisplayName("Deve lançar exceção para preço negativo")
        void shouldThrowExceptionForNegativePrice() {
            assertThrows(IllegalArgumentException.class, () ->
                    Product.create("Produto", "Desc", new BigDecimal("-1"), 10));
        }

        @Test
        @DisplayName("Deve lançar exceção para quantidade negativa")
        void shouldThrowExceptionForNegativeQuantity() {
            assertThrows(IllegalArgumentException.class, () ->
                    Product.create("Produto", "Desc", BigDecimal.TEN, -5));
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Deve atualizar produto com dados válidos")
        void shouldUpdateProductWithValidData() {
            Product product = Product.create("Original", "Desc", BigDecimal.TEN, 5);

            product.update("Atualizado", "Nova Desc", new BigDecimal("99.90"), 20);

            assertEquals("Atualizado", product.getName());
            assertEquals("Nova Desc", product.getDescription());
            assertEquals(new BigDecimal("99.90"), product.getPrice());
            assertEquals(20, product.getStockQuantity());
        }
    }

    @Nested
    @DisplayName("deactivate")
    class Deactivate {

        @Test
        @DisplayName("Deve desativar produto")
        void shouldDeactivateProduct() {
            Product product = Product.create("Produto", "Desc", BigDecimal.TEN, 10);
            assertTrue(product.isActive());

            product.deactivate();

            assertFalse(product.isActive());
        }
    }
}
