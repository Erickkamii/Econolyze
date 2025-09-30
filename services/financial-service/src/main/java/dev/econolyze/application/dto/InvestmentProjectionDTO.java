package dev.econolyze.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentProjectionDTO {
    private Long userId;
    private BigDecimal amountBlank;
    private BigDecimal amountCdi;
    private String category;
    private String description;
    private LocalDate date;
}
