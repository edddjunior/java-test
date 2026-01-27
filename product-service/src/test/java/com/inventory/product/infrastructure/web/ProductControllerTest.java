package com.inventory.product.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.product.application.dto.PageDTO;
import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.usecase.*;
import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.infrastructure.web.request.CreateProductRequest;
import com.inventory.product.infrastructure.web.request.UpdateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private UpdateProductUseCase updateProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockBean
    private GetProductUseCase getProductUseCase;

    @MockBean
    private ListProductsUseCase listProductsUseCase;

    private ProductDTO createProductDTO(UUID id, String name) {
        return new ProductDTO(id, name, "Descrição", new BigDecimal("99.90"), 10, Instant.now(), Instant.now());
    }

    @Nested
    @DisplayName("POST /api/v1/products")
    class CreateProduct {

        @Test
        @DisplayName("Deve criar produto com sucesso")
        void shouldCreateProductSuccessfully() throws Exception {
            UUID id = UUID.randomUUID();
            CreateProductRequest request = new CreateProductRequest("Notebook", "Desc", new BigDecimal("2999.90"), 25);
            ProductDTO dto = createProductDTO(id, "Notebook");

            when(createProductUseCase.execute(anyString(), anyString(), any(BigDecimal.class), anyInt()))
                    .thenReturn(dto);

            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.id").value(id.toString()))
                    .andExpect(jsonPath("$.data.name").value("Notebook"));
        }

        @Test
        @DisplayName("Deve retornar 400 para nome vazio")
        void shouldReturn400ForEmptyName() throws Exception {
            CreateProductRequest request = new CreateProductRequest("", "Desc", new BigDecimal("99.90"), 10);

            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve retornar 400 para preço negativo")
        void shouldReturn400ForNegativePrice() throws Exception {
            CreateProductRequest request = new CreateProductRequest("Produto", "Desc", new BigDecimal("-1"), 10);

            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Deve retornar 400 para JSON malformado")
        void shouldReturn400ForMalformedJson() throws Exception {
            mockMvc.perform(post("/api/v1/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products/{id}")
    class GetProduct {

        @Test
        @DisplayName("Deve retornar produto quando encontrado")
        void shouldReturnProductWhenFound() throws Exception {
            UUID id = UUID.randomUUID();
            ProductDTO dto = createProductDTO(id, "Notebook");
            when(getProductUseCase.execute(id)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/products/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(id.toString()))
                    .andExpect(jsonPath("$.data.name").value("Notebook"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando não encontrado")
        void shouldReturn404WhenNotFound() throws Exception {
            UUID id = UUID.randomUUID();
            when(getProductUseCase.execute(id)).thenThrow(new ProductNotFoundException(id));

            mockMvc.perform(get("/api/v1/products/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
        }

        @Test
        @DisplayName("Deve retornar 400 para UUID inválido")
        void shouldReturn400ForInvalidUuid() throws Exception {
            mockMvc.perform(get("/api/v1/products/{id}", "invalid-uuid"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("TYPE_MISMATCH"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products")
    class ListProducts {

        @Test
        @DisplayName("Deve retornar lista paginada")
        void shouldReturnPaginatedList() throws Exception {
            UUID id = UUID.randomUUID();
            ProductDTO dto = createProductDTO(id, "Notebook");
            PageDTO<ProductDTO> pageDTO = new PageDTO<>(List.of(dto), 0, 20, 1, 1, true, true);

            when(listProductsUseCase.execute(anyInt(), anyInt())).thenReturn(pageDTO);

            mockMvc.perform(get("/api/v1/products")
                            .param("page", "0")
                            .param("size", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].name").value("Notebook"))
                    .andExpect(jsonPath("$.data.first").value(true))
                    .andExpect(jsonPath("$.data.last").value(true));
        }

        @Test
        @DisplayName("Deve retornar lista vazia")
        void shouldReturnEmptyList() throws Exception {
            PageDTO<ProductDTO> pageDTO = new PageDTO<>(List.of(), 0, 20, 0, 0, true, true);

            when(listProductsUseCase.execute(anyInt(), anyInt())).thenReturn(pageDTO);

            mockMvc.perform(get("/api/v1/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content").isEmpty())
                    .andExpect(jsonPath("$.data.totalElements").value(0));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/products/{id}")
    class UpdateProduct {

        @Test
        @DisplayName("Deve atualizar produto com sucesso")
        void shouldUpdateProductSuccessfully() throws Exception {
            UUID id = UUID.randomUUID();
            UpdateProductRequest request = new UpdateProductRequest("Novo Nome", "Nova Desc", new BigDecimal("199.90"), 50);
            ProductDTO dto = createProductDTO(id, "Novo Nome");

            when(updateProductUseCase.execute(eq(id), anyString(), anyString(), any(BigDecimal.class), anyInt()))
                    .thenReturn(dto);

            mockMvc.perform(put("/api/v1/products/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("Novo Nome"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando produto não existe")
        void shouldReturn404WhenNotFound() throws Exception {
            UUID id = UUID.randomUUID();
            UpdateProductRequest request = new UpdateProductRequest("Nome", "Desc", new BigDecimal("99.90"), 10);

            when(updateProductUseCase.execute(eq(id), anyString(), anyString(), any(BigDecimal.class), anyInt()))
                    .thenThrow(new ProductNotFoundException(id));

            mockMvc.perform(put("/api/v1/products/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/products/{id}")
    class DeleteProduct {

        @Test
        @DisplayName("Deve deletar produto com sucesso")
        void shouldDeleteProductSuccessfully() throws Exception {
            UUID id = UUID.randomUUID();
            doNothing().when(deleteProductUseCase).execute(id);

            mockMvc.perform(delete("/api/v1/products/{id}", id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar 404 quando produto não existe")
        void shouldReturn404WhenNotFound() throws Exception {
            UUID id = UUID.randomUUID();
            doThrow(new ProductNotFoundException(id)).when(deleteProductUseCase).execute(id);

            mockMvc.perform(delete("/api/v1/products/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
        }
    }
}
