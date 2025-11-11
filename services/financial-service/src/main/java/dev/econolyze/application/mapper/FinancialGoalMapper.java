package dev.econolyze.application.mapper;

import dev.econolyze.application.converter.EnumConverter;
import dev.econolyze.application.dto.FinancialGoalDTO;
import dev.econolyze.application.dto.response.FinancialGoalResponse;
import dev.econolyze.domain.entity.FinancialGoal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", uses =  {EnumConverter.class})
public interface FinancialGoalMapper {
    FinancialGoalDTO mapToDTO(FinancialGoal financialGoal);
    FinancialGoalDTO mapToDTO(FinancialGoalResponse financialGoalResponse);
    FinancialGoal mapToEntity(FinancialGoalDTO financialGoalDTO);
    FinancialGoalResponse mapToResponse(FinancialGoal financialGoal);
}
