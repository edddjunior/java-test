package com.inventory.stock.infrastructure.client;

import com.inventory.stock.domain.model.ProductStock;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductClientAdapter")
class ProductClientAdapterTest {

    @Mock
    private ProductServiceClient productServiceClient;

    private ProductClientAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductClientAdapter(productServiceClient);
    }

    @Nested
    @DisplayName("getProduct")
    class GetProduct {

        @Test
        @DisplayName("Deve retornar ProductStock quando produto encontrado")
        void shouldReturnProductStockWhenFound() {
            UUID productId = UUID.randomUUID();
            var productData = new ProductServiceClient.ProductResponse.ProductData(
                    productId, "Notebook", new BigDecimal("2999.90"), 50
            );
            var response = new ProductServiceClient.ProductResponse(productData);

            when(productServiceClient.getProduct(productId)).thenReturn(response);

            Optional<ProductStock> result = adapter.getProduct(productId);

            assertTrue(result.isPresent());
            assertEquals(productId, result.get().id());
            assertEquals("Notebook", result.get().name());
            assertEquals(50, result.get().stockQuantity());
        }

        @Test
        @DisplayName("Deve retornar empty quando produto não encontrado (404)")
        void shouldReturnEmptyWhenNotFound() {
            UUID productId = UUID.randomUUID();
            Request request = Request.create(
                    Request.HttpMethod.GET,
                    "/api/v1/products/" + productId,
                    Collections.emptyMap(),
                    null,
                    new RequestTemplate()
            );
            FeignException.NotFound notFound = new FeignException.NotFound(
                    "Not Found", request, null, Collections.emptyMap()
            );

            when(productServiceClient.getProduct(productId)).thenThrow(notFound);

            Optional<ProductStock> result = adapter.getProduct(productId);

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar empty quando response é null")
        void shouldReturnEmptyWhenResponseIsNull() {
            UUID productId = UUID.randomUUID();

            when(productServiceClient.getProduct(productId)).thenReturn(null);

            Optional<ProductStock> result = adapter.getProduct(productId);

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar empty quando data é null")
        void shouldReturnEmptyWhenDataIsNull() {
            UUID productId = UUID.randomUUID();
            var response = new ProductServiceClient.ProductResponse(null);

            when(productServiceClient.getProduct(productId)).thenReturn(response);

            Optional<ProductStock> result = adapter.getProduct(productId);

            assertTrue(result.isEmpty());
        }
    }
}
