package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.domain.entity.Balance;
import org.mapstruct.Mapper;

@Mapper(config = QuarkusMapperConfig.class, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BalanceMapper {
        BalanceDTO mapToDTO(Balance balance);
}
