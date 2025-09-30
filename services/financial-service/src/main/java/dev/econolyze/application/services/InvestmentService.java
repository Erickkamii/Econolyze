package dev.econolyze.application.services;

import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.domain.enums.Estimate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class InvestmentService {
    @Inject
    CdiService cdiService;
    @Inject
    IncomeService incomeService;

    public InvestmentProjectionDTO getProjectionBasedOnCdiRate(Long userId, Estimate rate) {
        List<IncomeDTO> incomes = incomeService.getIncomesByUserIdAndCategory(userId, "investment");
        if(!incomes.isEmpty()){
            new InvestmentProjectionDTO();
            InvestmentProjectionDTO projection;
            projection = initializeProjection(incomes, userId);
            projection.setDescription("CDI Investment in " + rate + " rate");
            if (rate.equals(Estimate.YEARLY)) {
                BigDecimal cdiAnnualRate = getCdiAnnualRate();
                projection.setAmountCdi(projection.getAmountCdi().multiply(BigDecimal.ONE.add(cdiAnnualRate)).setScale(2, RoundingMode.HALF_UP));
            } else if(rate.equals(Estimate.MONTHLY)) {
                BigDecimal cdiMonthlyRate = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(getCdiAnnualRate()).doubleValue(), 1.0 / 12) - 1);
                projection.setAmountCdi(projection.getAmountCdi().multiply(BigDecimal.ONE.add(cdiMonthlyRate)).setScale(2, RoundingMode.HALF_UP));
            } else if (rate.equals(Estimate.DAILY)) {
                BigDecimal cdiDailyRate = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(getCdiAnnualRate()).doubleValue(), 1.0 / 252) - 1);
                projection.setAmountCdi(projection.getAmountCdi().multiply(BigDecimal.ONE.add(cdiDailyRate)).setScale(2, RoundingMode.HALF_UP));
            }
            return projection;
        }
        return InvestmentProjectionDTO.builder()
                .userId(userId)
                .amountBlank(BigDecimal.ZERO)
                .amountCdi(BigDecimal.ZERO)
                .date(LocalDate.now())
                .category("Investment")
                .description("No Investment registered")
                .build();
    }

    public InvestmentProjectionDTO getProjectionBasedOnCdiWithPercentage(Long userId, Estimate rate, BigDecimal percentage){
        InvestmentProjectionDTO projection = getProjectionBasedOnCdiRate(userId, rate);
        if(projection.getAmountCdi() != null){
            BigDecimal factor = percentage.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            projection.setAmountCdi(projection.getAmountBlank()
                    .multiply(
                            projection.getAmountCdi()
                                    .divide(projection.getAmountBlank(), 10, RoundingMode.HALF_UP)
                                    .subtract(BigDecimal.ONE)
                                    .multiply(factor)
                                    .add(BigDecimal.ONE)
                    ).setScale(2, RoundingMode.HALF_UP));
            projection.setDescription("CDI Investment in " + rate + " rate with " + percentage + " %");
        }
        return projection;
    }
    
    private InvestmentProjectionDTO initializeProjection(List<IncomeDTO> incomes, Long userId){
        InvestmentProjectionDTO projection = new InvestmentProjectionDTO();
        projection.setAmountCdi(BigDecimal.ZERO);
        incomes.forEach(income -> projection.setAmountCdi(projection.getAmountCdi().add(income.getAmount())));
        projection.setUserId(userId);
        projection.setDate(LocalDate.now());
        projection.setCategory("Investment");
        projection.setAmountBlank(projection.getAmountCdi());
        return projection;
    }

    private BigDecimal getCdiAnnualRate(){
        return cdiService.getCurrentCdiRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }
}
