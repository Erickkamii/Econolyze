package dev.econolyze.application.services;

import dev.econolyze.application.dto.IncomeDTO;
import dev.econolyze.application.dto.InvestmentProjectionDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class InvestmentService {
    @Inject
    TransactionService transactionService;
    @Inject
    CdiService cdiService;
    @Inject
    IncomeService incomeService;

    public InvestmentProjectionDTO getProjectionBasedOnCdiRate(Long userId){
        List<IncomeDTO> incomes = incomeService.getIncomesByUserIdAndCategory(userId, "investment");
        return null;
    }
}
