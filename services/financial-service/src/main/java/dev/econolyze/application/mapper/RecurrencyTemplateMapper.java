package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.RecurrencyTemplateDTO;
import dev.econolyze.domain.entity.RecurringTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface RecurrencyTemplateMapper {
    RecurrencyTemplateDTO mapToDTO(RecurringTemplate recurrencyTemplate);
    RecurringTemplate mapToEntity(RecurrencyTemplateDTO recurrencyTemplateDTO);
}
