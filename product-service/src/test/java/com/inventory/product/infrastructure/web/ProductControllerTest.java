package com.inventory.product.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.usecase.*;
import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.domain.model.PageResult;
import com.inventory.product.infrastructure.web.request.CreateProductRequest;
import com.inventory.product.infrastructure.web.request.UpdateProductRequest;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    @DisplayName("POST /api/products - Deve criar produto com sucesso")
    void shouldCreateProductSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        CreateProductRequest request = new CreateProductRequest("Notebook", "Desc", new BigDecimal("2999.90"), 25);
        ProductDTO dto = createProductDTO(id, "Notebook");

        when(createProductUseCase.execute(anyString(), anyString(), any(BigDecimal.class), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.name").value("Notebook"));
    }

    @Test
    @DisplayName("POST /api/products - Deve retornar 400 para dados inválidos")
    void shouldReturn400ForInvalidData() throws Exception {
        CreateProductRequest request = new CreateProductRequest("", null, new BigDecimal("-1"), -5);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Deve retornar produto quando encontrado")
    void shouldReturnProductWhenFound() throws Exception {
        UUID id = UUID.randomUUID();
        ProductDTO dto = createProductDTO(id, "Notebook");
        when(getProductUseCase.execute(id)).thenReturn(dto);

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.name").value("Notebook"));
    }

    @Test
    @DisplayName("GET /api/products/{id} - Deve retornar 404 quando não encontrado")
    void shouldReturn404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(getProductUseCase.execute(id)).thenThrow(new ProductNotFoundException(id));

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/products - Deve retornar lista paginada")
    void shouldReturnPaginatedList() throws Exception {
        UUID id = UUID.randomUUID();
        ProductDTO dto = createProductDTO(id, "Notebook");
        PageResult<ProductDTO> pageResult = new PageResult<>(List.of(dto), 0, 20, 1, 1);

        when(listProductsUseCase.execute(anyInt(), anyInt())).thenReturn(pageResult);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("Notebook"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - Deve atualizar produto com sucesso")
    void shouldUpdateProductSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateProductRequest request = new UpdateProductRequest("Novo Nome", "Nova Desc", new BigDecimal("199.90"), 50);
        ProductDTO dto = createProductDTO(id, "Novo Nome");

        when(updateProductUseCase.execute(eq(id), anyString(), anyString(), any(BigDecimal.class), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Novo Nome"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Deve deletar produto com sucesso")
    void shouldDeleteProductSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(deleteProductUseCase).execute(id);

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());
    }
}
