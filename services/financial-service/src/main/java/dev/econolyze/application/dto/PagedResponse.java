package dev.econolyze.application.dto;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.List;
import java.util.function.Function;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <E, D> PagedResponse<D> fromPanacheQuery(
            PanacheQuery<E> query, int page, int size, Function<E, D> mapper
    ) {
        return new PagedResponse<>(
                query.list().stream().map(mapper).toList(),
                page,
                size,
                query.count(),
                query.pageCount()
        );
    }
}
