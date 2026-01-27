package com.inventory.product.infrastructure.persistence.adapter;

import com.inventory.product.domain.model.PageResult;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import com.inventory.product.infrastructure.persistence.entity.ProductEntity;
import com.inventory.product.infrastructure.persistence.repository.JpaProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    public ProductRepositoryAdapter(JpaProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findByIdAndActiveTrue(id).map(this::toDomain);
    }

    @Override
    public PageResult<Product> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductEntity> entityPage = jpaRepository.findAllByActiveTrue(pageRequest);

        return new PageResult<>(
                entityPage.getContent().stream().map(this::toDomain).toList(),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages()
        );
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setActive(false);
            entity.setUpdatedAt(Instant.now());
            jpaRepository.save(entity);
        });
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsByIdAndActiveTrue(id);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getVersion()
        );
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getVersion()
        );
    }
}
