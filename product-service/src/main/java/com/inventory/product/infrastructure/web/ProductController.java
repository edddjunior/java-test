package com.inventory.product.infrastructure.web;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.usecase.*;
import com.inventory.product.domain.model.PageResult;
import com.inventory.product.infrastructure.web.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductUseCase getProductUseCase,
            ListProductsUseCase listProductsUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.listProductsUseCase = listProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @GetMapping
    public ApiResponse<PageResult<ProductDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.of(listProductsUseCase.execute(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> findById(@PathVariable UUID id) {
        return ApiResponse.of(getProductUseCase.execute(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> create(@RequestBody CreateProductRequest request) {
        ProductDTO dto = createProductUseCase.execute(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> update(@PathVariable UUID id, @RequestBody UpdateProductRequest request) {
        ProductDTO dto = updateProductUseCase.execute(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity()
        );
        return ApiResponse.of(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteProductUseCase.execute(id);
    }

    record CreateProductRequest(String name, String description, BigDecimal price, Integer stockQuantity) {}
    record UpdateProductRequest(String name, String description, BigDecimal price, Integer stockQuantity) {}
}
