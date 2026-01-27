package com.inventory.stock.infrastructure.web;

import com.inventory.stock.application.usecase.CheckStockUseCase;
import com.inventory.stock.application.usecase.CheckStockUseCase.StockCheckResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock", description = "Consulta de estoque")
public class StockController {

    private final CheckStockUseCase checkStockUseCase;

    public StockController(CheckStockUseCase checkStockUseCase) {
        this.checkStockUseCase = checkStockUseCase;
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Verifica estoque de um produto")
    public ResponseEntity<StockCheckResult> checkStock(@PathVariable UUID productId) {
        return checkStockUseCase.execute(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
