package dev.econolyze.application.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CdiRateDTO {
    private BigDecimal currentRate;
    private LocalDate lastUpdate;
    private String source;
}