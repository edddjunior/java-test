package com.inventory.product.domain.repository;

import com.inventory.product.domain.model.PageResult;
import com.inventory.product.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    List<Product> findAll();

    PageResult<Product> findAll(int page, int size);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
