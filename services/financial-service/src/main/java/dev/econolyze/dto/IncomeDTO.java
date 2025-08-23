package dev.econolyze.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDTO {
    private Long id;
    private String name;
    private Double amount;
    private String category;
    private String description;
    private String date;
}
