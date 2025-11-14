package dev.econolyze.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private String name;
    private String type;
    private java.math.BigDecimal actualBalance;
    private java.math.BigDecimal creditLimit;
    private Long userId;
    private Integer closingDate;
    private Boolean active;
}
