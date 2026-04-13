package dev.econolyze.dto.flter;

import jakarta.ws.rs.QueryParam;


public record TransactionFilter(
        @QueryParam("type") String type,
        @QueryParam("category") String category,
        @QueryParam("minValue") Long minValue,
        @QueryParam("maxValue") Long maxValue
) {}

