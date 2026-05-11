package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.RecurrencyTemplateDTO;
import dev.econolyze.application.dto.response.RecurringTemplateResponse;
import dev.econolyze.domain.entity.RecurringTemplate;
import org.mapstruct.Mapper;

@Mapper(config = QuarkusMapperConfig.class, uses = TransactionMapper.class)
public interface RecurrencyTemplateMapper {
    RecurrencyTemplateDTO mapToDTO(RecurringTemplate recurrencyTemplate);
    RecurringTemplate mapToEntity(RecurrencyTemplateDTO recurrencyTemplateDTO);
    RecurringTemplateResponse mapToResponse(RecurringTemplate recurrencyTemplate);
}
