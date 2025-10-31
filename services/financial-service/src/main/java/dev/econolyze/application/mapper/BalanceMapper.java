package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.domain.entity.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BalanceMapper {
        BalanceDTO mapToDTO(Balance balance);
}
