package dev.econolyze.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportDTO {
    private Long id;
    private String month;
    private Double income;
    private Double expenses;
    private Double balance;
}
