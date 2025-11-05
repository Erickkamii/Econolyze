package dev.econolyze.application.services;

import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.enums.Estimate;
import dev.econolyze.domain.enums.TransactionType;
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
    TransactionService transactionService;
    @Inject
    UserContext userContext;

    public InvestmentProjectionDTO getProjectionBasedOnCdiRate(Estimate rate) {
        Long userId = userContext.getUserId();
        List<TransactionDTO> incomes = transactionService.getTransactionByUserIdAndType(userId, TransactionType.INVESTMENT);
        if(!incomes.isEmpty()){
            new InvestmentProjectionDTO();
            InvestmentProjectionDTO projection;
            projection = initializeProjection(incomes, userId);
            BigDecimal cdiAnnualRate = getCdiAnnualRate();
            projection.setDescription("CDI Investment in " + rate + " rate");
            if (rate.equals(Estimate.YEARLY)) {
                projection.setAmountCdi(projection.getAmountCdi().multiply(BigDecimal.ONE.add(cdiAnnualRate)).setScale(2, RoundingMode.HALF_UP));
            } else if(rate.equals(Estimate.MONTHLY)) {
                BigDecimal cdiMonthlyRate = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(cdiAnnualRate).doubleValue(), 1.0 / 12) - 1);
                projection.setAmountCdi(projection.getAmountCdi().multiply(BigDecimal.ONE.add(cdiMonthlyRate)).setScale(2, RoundingMode.HALF_UP));
            } else if (rate.equals(Estimate.DAILY)) {
                BigDecimal cdiDailyRate = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(cdiAnnualRate).doubleValue(), 1.0 / 252) - 1);
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

    public InvestmentProjectionDTO getProjectionBasedOnCdiWithPercentage(Estimate rate, BigDecimal percentage){
        InvestmentProjectionDTO projection = getProjectionBasedOnCdiRate(rate);
        if(projection.getAmountCdi() != null){
            BigDecimal factor = percentage.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
            projection.setAmountCdi(projection.getAmountBlank()
                    .multiply(
                            projection.getAmountCdi()
                                    .divide(projection.getAmountBlank(), 10, RoundingMode.HALF_UP)
                                    .subtract(BigDecimal.ONE)
                                    .multiply(factor)
                                    .add(BigDecimal.ONE)
                    ).setScale(2, RoundingMode.HALF_EVEN));
            projection.setDescription("CDI Investment in " + rate + " rate with " + percentage + " %");
        }
        return projection;
    }
    
    private InvestmentProjectionDTO initializeProjection(List<TransactionDTO> incomes, Long userId){
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
