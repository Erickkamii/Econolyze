package dev.econolyze.application.services;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.domain.entity.FinancialGoal;
import dev.econolyze.infrastructure.repository.FinancialGoalRepository;
import dev.econolyze.application.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    FinancialGoalRepository financialGoalRepository;
    @Mock
    GoalService goalService;
    @Mock
    UserContext userContext;

    @InjectMocks
    AnalyticsService analyticsService;

    @Test
    void analyzeGoalProgress_computesPercentage() {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        dto.setId(1L);
        dto.setAmount(BigDecimal.valueOf(200));
        dto.setName("goal");

        GoalProgressDTO progress = analyticsService.analyzeGoalProgress(dto, BigDecimal.valueOf(50));

        assertEquals(25.0, progress.getProgress());
    }

    @Test
    void analyzeGoalProgress_zeroIncomes_returnsZeroProgress() {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        dto.setId(2L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setName("noIncome");

        GoalProgressDTO progress = analyticsService.analyzeGoalProgress(dto, BigDecimal.ZERO);
        assertEquals(0.0, progress.getProgress());
    }

    @Test
    void analyzeGoalProgress_amountZero_throwsArithmeticException() {
        FinancialGoalDTO dto = new FinancialGoalDTO();
        dto.setId(3L);
        dto.setAmount(BigDecimal.ZERO);
        dto.setName("badGoal");

        assertThrows(ArithmeticException.class, () -> analyticsService.analyzeGoalProgress(dto, BigDecimal.valueOf(10)));
    }

    @Test
    void analyzeAllGoalProgress_handlesEmpty() {
        when(userContext.getUserId()).thenReturn(1L);
        when(financialGoalRepository.getAllGoalsByUserId(1L)).thenReturn(List.of());

        List<GoalProgressDTO> list = analyticsService.analyzeAllGoalProgress();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void analyzeAllGoalProgress_withValidGoals_returnsProgressList() {
        when(userContext.getUserId()).thenReturn(1L);
        FinancialGoal goal = new FinancialGoal();
        goal.setId(10L);
        goal.setUserId(1L);
        when(financialGoalRepository.getAllGoalsByUserId(1L)).thenReturn(List.of(goal));

        GoalProgressDTO gp = new GoalProgressDTO();
        gp.setId(10L);
        gp.setName("g");
        gp.setProgress(50.0);
        when(goalService.getGoalProgress(10L)).thenReturn(gp);

        List<GoalProgressDTO> result = analyticsService.analyzeAllGoalProgress();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(50.0, result.get(0).getProgress());
    }

    @Test
    void analyzeAllGoalProgress_filtersNulls_and_handlesGoalServiceNull() {
        when(userContext.getUserId()).thenReturn(2L);
        FinancialGoal g1 = new FinancialGoal();
        g1.setId(null); // should be filtered
        FinancialGoal g2 = new FinancialGoal();
        g2.setId(20L);
        when(financialGoalRepository.getAllGoalsByUserId(2L)).thenReturn(Arrays.asList(g1, g2, null));

        when(goalService.getGoalProgress(20L)).thenReturn(null); // should become default GoalProgressDTO

        List<GoalProgressDTO> result = analyticsService.analyzeAllGoalProgress();
        assertNotNull(result);
        // Only g2 should produce an element, and since goalService returned null it becomes a default GoalProgressDTO
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
    }
}
