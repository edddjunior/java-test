package com.inventory.product.application.dto;

import com.inventory.product.domain.model.PageResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PageDTO")
class PageDTOTest {

    private static final Function<String, String> IDENTITY = s -> s;

    @Nested
    @DisplayName("from")
    class From {

        @Test
        @DisplayName("Deve mapear PageResult para PageDTO corretamente")
        void shouldMapPageResultToPageDTO() {
            PageResult<String> pageResult = new PageResult<>(
                    List.of("item1", "item2"),
                    0,
                    20,
                    2,
                    1
            );

            PageDTO<String> result = PageDTO.from(pageResult, IDENTITY);

            assertEquals(2, result.content().size());
            assertEquals("item1", result.content().get(0));
            assertEquals(0, result.page());
            assertEquals(20, result.size());
            assertEquals(2, result.totalElements());
            assertEquals(1, result.totalPages());
        }

        @Test
        @DisplayName("Deve indicar first=true na primeira página")
        void shouldIndicateFirstPage() {
            PageResult<String> pageResult = new PageResult<>(List.of("item"), 0, 10, 100, 10);

            PageDTO<String> result = PageDTO.from(pageResult, IDENTITY);

            assertTrue(result.first());
            assertFalse(result.last());
        }

        @Test
        @DisplayName("Deve indicar first=false em página intermediária")
        void shouldIndicateNotFirstPage() {
            PageResult<String> pageResult = new PageResult<>(List.of("item"), 5, 10, 100, 10);

            PageDTO<String> result = PageDTO.from(pageResult, IDENTITY);

            assertFalse(result.first());
            assertFalse(result.last());
        }

        @Test
        @DisplayName("Deve indicar last=true na última página")
        void shouldIndicateLastPage() {
            PageResult<String> pageResult = new PageResult<>(List.of("item"), 9, 10, 100, 10);

            PageDTO<String> result = PageDTO.from(pageResult, IDENTITY);

            assertFalse(result.first());
            assertTrue(result.last());
        }

        @Test
        @DisplayName("Deve indicar first=true e last=true quando há apenas uma página")
        void shouldIndicateFirstAndLastWhenSinglePage() {
            PageResult<String> pageResult = new PageResult<>(List.of("item"), 0, 10, 1, 1);

            PageDTO<String> result = PageDTO.from(pageResult, IDENTITY);

            assertTrue(result.first());
            assertTrue(result.last());
        }

        @Test
        @DisplayName("Deve indicar last=true quando totalPages é zero (resultado vazio)")
        void shouldIndicateLastWhenEmpty() {
            PageResult<String> pageResult = new PageResult<>(List.of(), 0, 10, 0, 0);

            PageDTO<String> result = PageDTO.from(pageResult, IDENTITY);

            assertTrue(result.first());
            assertTrue(result.last());
            assertTrue(result.content().isEmpty());
        }

        @Test
        @DisplayName("Deve aplicar função de mapeamento corretamente")
        void shouldApplyMappingFunction() {
            PageResult<Integer> pageResult = new PageResult<>(List.of(1, 2, 3), 0, 10, 3, 1);

            PageDTO<String> result = PageDTO.from(pageResult, Object::toString);

            assertEquals(List.of("1", "2", "3"), result.content());
        }
    }
}
