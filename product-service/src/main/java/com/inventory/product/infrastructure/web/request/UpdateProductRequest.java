package com.inventory.product.infrastructure.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome não pode exceder 255 caracteres")
        String name,

        String description,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "Quantidade em estoque é obrigatória")
        @PositiveOrZero(message = "Quantidade não pode ser negativa")
        Integer stockQuantity
) {}
