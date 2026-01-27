package com.inventory.product.infrastructure.web.exception;

import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.infrastructure.web.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(ProductNotFoundException ex, HttpServletRequest req) {
        return response(ErrorCode.PRODUCT_NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handle(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCode.VALIDATION_ERROR.getDefaultMessage());
        return response(ErrorCode.VALIDATION_ERROR, message, req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handle(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String message = "Parâmetro '" + ex.getName() + "' possui valor inválido: " + ex.getValue();
        return response(ErrorCode.TYPE_MISMATCH, message, req);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorResponse> handle(ObjectOptimisticLockingFailureException ex, HttpServletRequest req) {
        return response(ErrorCode.CONCURRENT_MODIFICATION, "Recurso foi modificado por outro usuário", req);
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
