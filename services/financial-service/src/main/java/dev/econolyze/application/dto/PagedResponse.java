package dev.econolyze.application.dto;

import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.function.Function;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <E, D> Uni<PagedResponse<D>> from(
            Uni<List<E>> listUni,
            Uni<Long> countUni,
            int page,
            int size,
            Function<E, D> mapper
    ) {
        return Uni.combine().all().unis(listUni, countUni)
                .asTuple()
                .map(tuple -> {
                    List<D> content = tuple.getItem1().stream().map(mapper).toList();
                    long total = tuple.getItem2();
                    int totalPages = (int) Math.ceil((double) total / size);
                    return new PagedResponse<>(content, page, size, total, totalPages);
                });
    }
}
