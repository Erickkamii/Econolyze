package dev.econolyze.application.services;

import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.GoalProgressDTO;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.infrastructure.repository.FinancialGoalRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    @Inject
    FinancialGoalRepository financialGoalRepository;
    @Inject
    GoalService goalService;
    @Inject
    UserContext userContext;

    public GoalProgressDTO analyzeGoalProgress(FinancialGoalDTO financialGoal, BigDecimal incomesSum) {
        GoalProgressDTO goalProgressDTO = new GoalProgressDTO();
        goalProgressDTO.setId(financialGoal.getId());
        goalProgressDTO.setName(financialGoal.getName());
        // Use a reasonable scale to preserve fractional progress (e.g., 10 decimal places)
        goalProgressDTO.setProgress(incomesSum
                .divide(financialGoal.getAmount(), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue());
        return goalProgressDTO;
    }

    @WithSession
    public Uni<List<GoalProgressDTO>> analyzeAllGoalProgress(){
        Long userId = userContext.getUserId();
        return financialGoalRepository.getAllGoalsByUserId(userId)
                .flatMap(goals -> {
                    List<Uni<GoalProgressDTO>> tasks = goals.stream()
                            .filter(Objects::nonNull)
                            .filter(goal -> goal.getId() != null)
                            .map(goal -> goalService.getGoalProgress(goal.getId())
                                    .map(progress -> progress != null ? progress : new GoalProgressDTO()))
                            .toList();

                    if (tasks.isEmpty()) return Uni.createFrom().item(List.of());
                    return Uni.combine().all().unis(tasks).with(list ->
                            list.stream().map(o-> (GoalProgressDTO) o).toList());
                });
    }
}
