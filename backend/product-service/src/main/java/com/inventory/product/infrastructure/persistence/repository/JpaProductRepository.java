package com.inventory.product.infrastructure.persistence.repository;

import com.inventory.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByIdAndActiveTrue(UUID id);

    Page<ProductEntity> findAllByActiveTrue(Pageable pageable);

    boolean existsByIdAndActiveTrue(UUID id);
}
