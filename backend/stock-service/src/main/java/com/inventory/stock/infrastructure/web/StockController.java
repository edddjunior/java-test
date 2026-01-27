package com.inventory.stock.infrastructure.web;

import com.inventory.stock.application.dto.StockDTO;
import com.inventory.stock.application.usecase.CheckStockUseCase;
import com.inventory.stock.domain.exception.ProductNotFoundException;
import com.inventory.stock.infrastructure.web.response.ApiResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock")
@Tag(name = "Stock", description = "Consulta de estoque")
@RateLimiter(name = "stockApi")
public class StockController {

    private final CheckStockUseCase checkStockUseCase;

    public StockController(CheckStockUseCase checkStockUseCase) {
        this.checkStockUseCase = checkStockUseCase;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Consulta estoque de um produto")
    public ApiResponse<StockDTO> checkStock(@PathVariable UUID productId) {
        return checkStockUseCase.execute(productId)
                .map(ApiResponse::of)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
