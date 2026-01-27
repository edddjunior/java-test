package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetProductUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public ProductDTO execute(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
