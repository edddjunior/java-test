package com.inventory.stock.infrastructure.web;

import com.inventory.stock.application.dto.StockDTO;
import com.inventory.stock.application.usecase.CheckStockUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
@DisplayName("StockController")
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckStockUseCase checkStockUseCase;

    @Nested
    @DisplayName("GET /api/v1/stock/{productId}")
    class CheckStock {

        @Test
        @DisplayName("Deve retornar estoque quando produto encontrado")
        void shouldReturnStockWhenFound() throws Exception {
            UUID productId = UUID.randomUUID();
            StockDTO result = new StockDTO(productId, "Notebook", 50);

            when(checkStockUseCase.execute(productId)).thenReturn(Optional.of(result));

            mockMvc.perform(get("/api/v1/stock/{productId}", productId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.productId").value(productId.toString()))
                    .andExpect(jsonPath("$.data.productName").value("Notebook"))
                    .andExpect(jsonPath("$.data.quantity").value(50));
        }

        @Test
        @DisplayName("Deve retornar 404 quando produto não encontrado")
        void shouldReturn404WhenNotFound() throws Exception {
            UUID productId = UUID.randomUUID();

            when(checkStockUseCase.execute(productId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/stock/{productId}", productId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("PRODUCT_NOT_FOUND"));
        }

        @Test
        @DisplayName("Deve retornar 400 para UUID inválido")
        void shouldReturn400ForInvalidUuid() throws Exception {
            mockMvc.perform(get("/api/v1/stock/{productId}", "invalid-uuid"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("TYPE_MISMATCH"));
        }
    }
}
