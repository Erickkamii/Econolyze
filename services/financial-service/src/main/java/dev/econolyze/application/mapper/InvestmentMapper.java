package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.application.dto.response.InvestmentProjectionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface InvestmentMapper {
    InvestmentProjectionResponse mapToResponse(InvestmentProjectionDTO dto);
    InvestmentProjectionDTO mapToDTO(InvestmentProjectionResponse projection);
}
