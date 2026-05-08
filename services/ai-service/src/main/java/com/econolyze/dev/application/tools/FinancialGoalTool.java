package com.econolyze.dev.application.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class FinancialGoalTool {

    @Tool("Calcula quanto o usuário precisa guardar por mês para atingir a meta")
    public String calculateRequiredMonths(BigDecimal targetAmount, BigDecimal currentAmount, Integer months){
        BigDecimal missing = targetAmount.subtract(currentAmount);
        BigDecimal monthly = missing.divide(BigDecimal.valueOf(months), 2, BigDecimal.ROUND_HALF_UP);
        return "Será necessário guardar R$"+monthly;
    }

    @Tool("Calcula quantos meses o usuário irá atingir uma meta guardando um valor mensal")
    public String calculateMonthsGoal(BigDecimal targetAmount, BigDecimal currentAmount, BigDecimal monthly){
        BigDecimal missing = targetAmount.subtract(currentAmount);
        int months = missing.divide(monthly, 2, BigDecimal.ROUND_HALF_UP).intValue();
        return "Serão necessários aproximadamente "+months+" meses";
    }
}
