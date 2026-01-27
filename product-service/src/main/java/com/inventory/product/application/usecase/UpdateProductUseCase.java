package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.exception.ProductNotFoundException;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public UpdateProductUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductDTO execute(UUID id, String name, String description, BigDecimal price, Integer stockQuantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.update(name, description, price, stockQuantity);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }
}
