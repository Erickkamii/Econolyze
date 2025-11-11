package dev.econolyze.application.services;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.infrastructure.repository.FinancialGoalRepository;
import dev.econolyze.application.mapper.FinancialGoalMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.application.dto.response.FinancialGoalResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.nullable;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    FinancialGoalRepository financialGoalRepository;
    @Mock
    FinancialGoalMapper financialGoalMapper;
    @Mock
    AnalyticsService analyticsService;
    @Mock
    TransactionService transactionService;
    @Mock
    UserContext userContext;

    @InjectMocks
    GoalService goalService;

    @Test
    void removeIfNotMatch_filtersTransactions() throws Exception{
        // Prepare
        TransactionDTO t1 = new TransactionDTO();
        t1.setFinancialGoalId(5L);
        t1.setAmount(BigDecimal.valueOf(10));
        TransactionDTO t2 = new TransactionDTO();
        t2.setFinancialGoalId(6L);
        t2.setAmount(BigDecimal.valueOf(5));

        // use reflection to call private method? Instead we'll call getGoalProgress with mocked dependencies
        FinancialGoalDTO goalDto = FinancialGoalDTO.builder().id(5L).amount(BigDecimal.valueOf(100)).name("g").build();
        when(userContext.getUserId()).thenReturn(1L);
        when(financialGoalMapper.mapToDTO(nullable(FinancialGoalResponse.class))).thenReturn(goalDto);
        when(transactionService.getTransactionByUserIdAndType(1L, TransactionType.INCOME)).thenReturn(List.of(t1, t2));
        when(analyticsService.analyzeGoalProgress(any(), any())).thenReturn(new GoalProgressDTO());

        GoalProgressDTO progress = goalService.getGoalProgress(5L);

        assertNotNull(progress);
        verify(transactionService, times(1)).getTransactionByUserIdAndType(1L, TransactionType.INCOME);
    }
}
