package com.inventory.product.application.dto;

import com.inventory.product.domain.model.PageResult;

import java.util.List;
import java.util.function.Function;

public record PageDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <S, T> PageDTO<T> from(PageResult<S> pageResult, Function<S, T> mapper) {
        List<T> mappedContent = pageResult.content().stream()
                .map(mapper)
                .toList();

        boolean isFirst = pageResult.page() == 0;
        boolean isLast = pageResult.totalPages() == 0 || pageResult.page() >= pageResult.totalPages() - 1;

        return new PageDTO<>(
                mappedContent,
                pageResult.page(),
                pageResult.size(),
                pageResult.totalElements(),
                pageResult.totalPages(),
                isFirst,
                isLast
        );
    }
}
