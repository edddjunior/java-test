package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CreateProductUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductDTO execute(String name, String description, BigDecimal price, Integer stockQuantity) {
        Product product = Product.create(name, description, price, stockQuantity);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }
}
