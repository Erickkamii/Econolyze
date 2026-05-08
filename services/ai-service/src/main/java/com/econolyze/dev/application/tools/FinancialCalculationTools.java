package com.econolyze.dev.application.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class FinancialCalculationTools {

    @Tool("Calcula o saldo final com saldo inicial, depósito mensal e quantidade de meses")
    public String simulateFutureBalance(BigDecimal initialBalance, BigDecimal monthlyDeposit, Integer months) {
        BigDecimal total = initialBalance.add(monthlyDeposit.multiply(BigDecimal.valueOf(months)));
        return "Saldo final: R$ " + total;
    }

    @Tool("Calcula o saldo líquido subtraindo despesas das receitas")
    public String calculateNetBalance(BigDecimal income, BigDecimal expenses) {
        return "Saldo líquido: R$ " + income.subtract(expenses);
    }
}
