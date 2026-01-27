package com.inventory.stock.infrastructure.web.exception;

import com.inventory.stock.domain.exception.ProductNotFoundException;
import com.inventory.stock.infrastructure.web.response.ApiErrorResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(ProductNotFoundException ex, HttpServletRequest req) {
        return response(ErrorCode.PRODUCT_NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorResponse> handle(FeignException ex, HttpServletRequest req) {
        return response(ErrorCode.PRODUCT_SERVICE_UNAVAILABLE, req);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<ApiErrorResponse> handle(CallNotPermittedException ex, HttpServletRequest req) {
        return response(ErrorCode.SERVICE_UNAVAILABLE, "Serviço temporariamente indisponível. Tente novamente.", req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handle(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String message = "Parâmetro '" + ex.getName() + "' possui valor inválido: " + ex.getValue();
        return response(ErrorCode.TYPE_MISMATCH, message, req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handle(IllegalArgumentException ex, HttpServletRequest req) {
        return response(ErrorCode.INVALID_ARGUMENT, ex.getMessage(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handle(Exception ex, HttpServletRequest req) {
        return response(ErrorCode.INTERNAL_ERROR, req);
    }

    private ResponseEntity<ApiErrorResponse> response(ErrorCode code, HttpServletRequest req) {
        return response(code, code.getDefaultMessage(), req);
    }

    private ResponseEntity<ApiErrorResponse> response(ErrorCode code, String message, HttpServletRequest req) {
        return ResponseEntity.status(code.getStatus())
                .body(ApiErrorResponse.of(code.name(), message, req.getRequestURI()));
    }
}
