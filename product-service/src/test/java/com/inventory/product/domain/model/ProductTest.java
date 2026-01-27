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
        @DisplayName("Deve lançar exceção para nome com mais de 255 caracteres")
        void shouldThrowExceptionForNameTooLong() {
            String longName = "a".repeat(256);
            assertThrows(IllegalArgumentException.class, () ->
                    Product.create(longName, "Desc", BigDecimal.TEN, 10));
        }

        @Test
        @DisplayName("Deve aceitar nome com exatamente 255 caracteres")
        void shouldAcceptNameWith255Characters() {
            String name = "a".repeat(255);
            Product product = Product.create(name, "Desc", BigDecimal.TEN, 10);
            assertEquals(255, product.getName().length());
        }

        @Test
        @DisplayName("Deve lançar exceção para preço zero")
        void shouldThrowExceptionForZeroPrice() {
            assertThrows(IllegalArgumentException.class, () ->
                    Product.create("Produto", "Desc", BigDecimal.ZERO, 10));
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

        @Test
        @DisplayName("Deve aceitar quantidade zero")
        void shouldAcceptZeroQuantity() {
            Product product = Product.create("Produto", "Desc", BigDecimal.TEN, 0);
            assertEquals(0, product.getStockQuantity());
        }

        @Test
        @DisplayName("Deve fazer trim do nome")
        void shouldTrimName() {
            Product product = Product.create("  Notebook  ", "Desc", BigDecimal.TEN, 10);
            assertEquals("Notebook", product.getName());
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

        @Test
        @DisplayName("Deve atualizar updatedAt ao modificar")
        void shouldUpdateTimestamp() throws InterruptedException {
            Product product = Product.create("Original", "Desc", BigDecimal.TEN, 5);
            var originalUpdatedAt = product.getUpdatedAt();

            Thread.sleep(10);
            product.update("Novo", "Desc", BigDecimal.TEN, 5);

            assertTrue(product.getUpdatedAt().isAfter(originalUpdatedAt));
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
