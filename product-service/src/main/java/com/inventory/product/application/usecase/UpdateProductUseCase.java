package com.inventory.product.application.usecase;

import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateProductUseCase {

    private final ProductRepository productRepository;

    public UpdateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> execute(UUID id, String name, String description, BigDecimal price, Integer stockQuantity) {
        return productRepository.findById(id)
                .map(product -> {
                    product.update(name, description, price, stockQuantity);
                    return productRepository.save(product);
                });
    }
}
