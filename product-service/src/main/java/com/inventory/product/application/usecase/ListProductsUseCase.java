package com.inventory.product.application.usecase;

import com.inventory.product.application.dto.PageDTO;
import com.inventory.product.application.dto.ProductDTO;
import com.inventory.product.application.mapper.ProductMapper;
import com.inventory.product.domain.model.PageResult;
import com.inventory.product.domain.model.Product;
import com.inventory.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListProductsUseCase {

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ListProductsUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public PageDTO<ProductDTO> execute(int page, int size) {
        int validPage = Math.max(0, page);
        int validSize = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);

        PageResult<Product> result = productRepository.findAll(validPage, validSize);

        return PageDTO.from(result, productMapper::toDTO);
    }
}
