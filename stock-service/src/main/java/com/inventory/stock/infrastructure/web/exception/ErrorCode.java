package com.inventory.stock.infrastructure.web.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 4xx - Client Errors
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Produto não encontrado"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Erro de validação"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "Argumento inválido"),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Tipo incompatível"),

    // 429 - Too Many Requests
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Limite de requisições excedido"),

    // 5xx - Server Errors
    PRODUCT_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de produtos indisponível"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Serviço temporariamente indisponível"),
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
