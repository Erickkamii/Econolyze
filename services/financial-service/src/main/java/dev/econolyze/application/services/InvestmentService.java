package dev.econolyze.application.services;

import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.InvestmentProjectionRequest;
import dev.econolyze.application.dto.response.InvestmentProjectionResponse;
import dev.econolyze.application.mapper.InvestmentMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.enums.Estimate;
import dev.econolyze.domain.enums.TransactionType;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
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
    @Inject
    InvestmentMapper investmentMapper;

    @WithSession
    public Uni<InvestmentProjectionResponse> getProjectionBasedOnCdiRate(Estimate rate) {
        return transactionService.getTransactionByUserIdAndType(userContext.getUserId(), TransactionType.INVESTMENT)
                .map(i -> {
                    if (i.isEmpty()){
                        return investmentMapper.mapToResponse(InvestmentProjectionDTO.builder()
                                .userId(userContext.getUserId())
                                .amountBlank(BigDecimal.ZERO)
                                .amountCdi(BigDecimal.ZERO)
                                .date(LocalDate.now())
                                .category("Investment")
                                .description("No investments found for the user.")
                                .build());
                    }
                    InvestmentProjectionDTO projection = initializeProjection(i, userContext.getUserId());
                    BigDecimal cdiAnnualRate = getCdiAnnualRate();
                    projection.setDescription("CDI Investment in " + rate + " rate");

                    if (rate.equals(Estimate.YEARLY)){
                        projection.setAmountCdi(projection.getAmountCdi()
                                .multiply(BigDecimal.ONE.add(cdiAnnualRate))
                                .setScale(2, RoundingMode.HALF_UP));
                    } else if (rate.equals(Estimate.MONTHLY)){
                        BigDecimal cdiMonthlyRate = BigDecimal.valueOf(
                                Math.pow(BigDecimal.ONE.add(cdiAnnualRate).doubleValue(), 1.0/12) - 1);
                        projection.setAmountCdi(projection.getAmountCdi()
                                .multiply(BigDecimal.ONE.add(cdiMonthlyRate))
                                .setScale(2, RoundingMode.HALF_UP));
                    } else if (rate.equals(Estimate.DAILY)){
                        BigDecimal cdiDailyRate = BigDecimal.valueOf(
                                Math.pow(BigDecimal.ONE.add(cdiAnnualRate).doubleValue(), 1.0/252) - 1);
                        projection.setAmountCdi(projection.getAmountCdi()
                                .multiply(BigDecimal.ONE.add(cdiDailyRate))
                                .setScale(2, RoundingMode.HALF_UP));
                    }
                    return investmentMapper.mapToResponse(projection);
                });
    }

    @WithSession
    public Uni<InvestmentProjectionResponse> getProjectionBasedOnCdiWithPercentage(Estimate rate, BigDecimal percentage){
        return getProjectionBasedOnCdiRate(rate)
                .map(r ->{
                    InvestmentProjectionDTO projection = investmentMapper.mapToDTO(r);
                    if(projection.getAmountCdi() != null){
                        BigDecimal factor = percentage.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
                        projection.setAmountCdi(projection.getAmountBlank()
                                        .multiply(projection.getAmountCdi()
                                                .divide(projection.getAmountBlank(), 10, RoundingMode.HALF_UP)
                                                .subtract(BigDecimal.ONE)
                                                .multiply(factor)
                                                .add(BigDecimal.ONE))
                                        .setScale(2, RoundingMode.HALF_EVEN));
                        projection.setDescription(projection.getDescription() + " with " + percentage + "% of CDI");
                    }
                    return investmentMapper.mapToResponse(projection);
                });
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

    @WithSession
    public Uni<InvestmentProjectionResponse> calculateProjection(InvestmentProjectionRequest request) {
        if (request == null || request.amount() == null || request.months() == null)
            return Uni.createFrom().failure(new IllegalArgumentException("Amount and months cannot be null"));
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0)
            return Uni.createFrom().failure(new IllegalArgumentException("Amount must be greater than zero"));
        if (request.months()<0)
            return Uni.createFrom().failure(new IllegalArgumentException("Months must be greater than zero"));
        BigDecimal amount = request.amount();
        Integer months = request.months();
        BigDecimal cdiAnnualRate = getCdiAnnualRate();
        BigDecimal cdiMonthlyRate = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(cdiAnnualRate).doubleValue(), 1.0 / 12) - 1);
        BigDecimal accumulatedFactor = BigDecimal.valueOf(Math.pow(BigDecimal.ONE.add(cdiMonthlyRate).doubleValue(), months));
        BigDecimal amountCdi = amount.multiply(accumulatedFactor).setScale(2, RoundingMode.HALF_UP);
        return Uni.createFrom().item(
                new InvestmentProjectionResponse(amount, amountCdi, "Investment", "Cdi Projection for "+months+" months", LocalDate.now())
        );
    }
}
