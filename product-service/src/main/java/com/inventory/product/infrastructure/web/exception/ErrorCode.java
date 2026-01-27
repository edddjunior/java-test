package com.inventory.product.infrastructure.web.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 4xx - Client Errors
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Produto não encontrado"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Erro de validação"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "Argumento inválido"),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Tipo incompatível"),

    // 409 - Conflict
    CONCURRENT_MODIFICATION(HttpStatus.CONFLICT, "Modificação concorrente"),

    // 429 - Too Many Requests
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições excedido"),

    // 5xx - Server Errors
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
