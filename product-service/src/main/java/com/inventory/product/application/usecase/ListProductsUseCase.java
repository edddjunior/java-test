package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.domain.model.PageResult;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ListProductsUseCase {

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private final ProductRepository productRepository;

    public ListProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PageResult<ProductDTO> execute(int page, int size) {
        int validPage = Math.max(0, page);
        int validSize = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);

        PageResult<Product> result = productRepository.findAll(validPage, validSize);

        return new PageResult<>(
                result.content().stream().map(ProductDTO::from).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
