package com.econolyze.dev.application.tools;

import com.econolyze.dev.infrastructure.clients.BalanceClient;
import com.econolyze.dev.infrastructure.clients.CdiClient;
import com.econolyze.dev.infrastructure.clients.GoalClient;
import com.econolyze.dev.infrastructure.clients.TransactionClient;
import com.econolyze.dev.infrastructure.security.UserContext;
import com.econolyze.dev.interfaces.rest.dto.InvestmentProjectionRequest;
import com.econolyze.dev.interfaces.rest.dto.TransactionByCategoryResponse;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class FinancialDataTools {

    @Inject
    @RestClient
    BalanceClient balanceClient;

    @Inject
    @RestClient
    TransactionClient transactionClient;

    @Inject
    @RestClient
    CdiClient cdiClient;

    @Inject
    @RestClient
    GoalClient goalClient;

    @Inject
    UserContext userContext;

    @Tool("Busca o saldo do Usuário")
    public String getCurrentBalance(){
        return balanceClient.getBalance(userContext.getToken())
                .map(t -> t.getEntity().balance())
                .await().indefinitely()
                .toString();
    }

    @Tool("Busca resumo financeiro por período, com receitas, despesas e saldo líquido")
    public String getFinancialSummary(String startDate, String endDate){
        return transactionClient.getTransactionSummary(
                startDate,
                endDate,
                userContext.getToken()
        ).map(r -> r.getEntity().toString()).await().indefinitely();
    }

    @Tool("""
        Busca transações agrupadas por categoria em um período.
        Use type como EXPENSE para gastos/despesas ou INCOME para receitas/entradas.
        Para perguntas como "quanto gastei em alimentação", use type EXPENSE.
        Mapeamentos comuns:
        - alimentação, comida, mercado, restaurante -> FOOD
        - transporte, carro, combustível, pneu -> TRANSPORT
        - contas, luz, internet, água -> UTILITIES
    """)
    public String getTransactionsByCategory(String startDate, String endDate, String type) {
        try {
            RestResponse<List<TransactionByCategoryResponse>> response =
                    transactionClient.getTransactionsByCategory(
                            startDate,
                            endDate,
                            type,
                            userContext.getToken()
                    ).await().indefinitely();

            if (response.getStatus() >= 400) {
                return "Erro ao buscar categorias. Status: " + response.getStatus();
            }

            List<TransactionByCategoryResponse> entity = response.getEntity();

            if (entity == null || entity.isEmpty()) {
                return "Nenhuma transação por categoria encontrada para o período informado.";
            }

            return entity.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar transações por categoria: "
                    + e.getClass().getSimpleName()
                    + " - "
                    + e.getMessage();
        }
    }

    @Tool("""
    Busca o total de uma categoria específica em um período.
    Use type EXPENSE para gastos e INCOME para receitas.
    Mapeamentos:
    alimentação, comida, mercado, restaurante -> FOOD
    transporte, carro, combustível, pneu -> TRANSPORT
    contas, luz, internet, água -> UTILITIES
    """)
    public String getCategoryTotal(String startDate, String endDate, String type, String category) {
        try {
            RestResponse<List<TransactionByCategoryResponse>> response =
                    transactionClient.getTransactionsByCategory(
                            startDate,
                            endDate,
                            type,
                            userContext.getToken()
                    ).await().indefinitely();

            List<TransactionByCategoryResponse> categories = response.getEntity();

            if (categories == null || categories.isEmpty()) {
                return "Nenhum valor encontrado para categorias nesse período.";
            }

            return categories.stream()
                    .filter(c -> c.category() != null)
                    .filter(c -> c.category().toString().equalsIgnoreCase(category))
                    .findFirst()
                    .map(Object::toString)
                    .orElse("Nenhum valor encontrado para a categoria " + category + " no período informado.");

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao buscar total por categoria: "
                    + e.getClass().getSimpleName()
                    + " - "
                    + e.getMessage();
        }
    }

    @Tool("Lista as transações recentes do usuário")
    public String getRecentTransactions(Integer limit){
//        return transactionClient.get
        return "Em progresso";
    }


    @Tool("""
    Obtém a taxa CDI atual oficial cadastrada no sistema Econolyze.
    Use SEMPRE esta ferramenta quando o usuário perguntar sobre taxa CDI atual,
    rendimento baseado em CDI, projeção com CDI ou comparação com CDI.
    Nunca estime a taxa CDI por conhecimento próprio.
    """)
    public String getTaxaCdi() {
        try {
            var response = cdiClient.getCdiRate(userContext.getToken());

            System.out.println("CDI status = " + response.getStatus());
            System.out.println("CDI entity = " + response.getEntity());

            if (response.getStatus() >= 400 || response.getEntity() == null) {
                return "Não foi possível consultar a taxa CDI atual no sistema. Status: " + response.getStatus();
            }

            var cdi = response.getEntity();

            return "Taxa CDI atual oficial do sistema Econolyze: "
                    + cdi.currentRate()
                    + "% ao ano. Última atualização: "
                    + cdi.lastUpdate()
                    + ". Fonte: "
                    + cdi.source()
                    + ".";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao consultar a taxa CDI atual no sistema: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
    @Tool("""
    Simula uma projeção de investimento baseada na taxa CDI oficial do sistema Econolyze.
    Use esta ferramenta quando o usuário informar valor e prazo para simular rendimento no CDI.
    """)
    public String simulateInvestmentProjection(BigDecimal amount, Integer months){
        return goalClient.simulateProjection(new InvestmentProjectionRequest(amount, months), userContext.getToken()).map(t -> t.getEntity().toString()).await().indefinitely();
    }

    @Tool("Busca as metas ativas do usuário")
    public String getActiveGoals(){
        return goalClient.getActiveGoals(userContext.getToken()).map(r -> r.getEntity().toString()).await().indefinitely();
    }

    @Tool("Busca metas financeiras do usuário pelo nome")
    public String getGoalByName(String name){
        return goalClient.getGoalsByName(name,  userContext.getToken()).map(t -> t.getEntity().toString()).await().indefinitely();
    }

}
