package dev.econolyze.application.services;

import dev.econolyze.application.dto.RecurrencyTemplateDTO;
import dev.econolyze.application.dto.response.RecurringTemplateResponse;
import dev.econolyze.application.mapper.RecurrencyTemplateMapper;
import dev.econolyze.application.mapper.TransactionMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.RecurringTemplate;
import dev.econolyze.domain.enums.RecurrenceFrequency;
import dev.econolyze.infrastructure.repository.RecurrencyTemplateRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecurringTransactionServiceTest {

    @Mock
    RecurrencyTemplateRepository recurrencyTemplateRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    RecurrencyTemplateMapper recurrencyTemplateMapper;
    @Mock
    TransactionMapper transactionMapper;
    @Mock
    UserContext userContext;

    @InjectMocks
    RecurringTransactionService service;

    @Test
    void createRecurringFromDTO_persistsTemplate() {
        RecurrencyTemplateDTO dto = new RecurrencyTemplateDTO(
                null, 1L, BigDecimal.TEN, null, null, null, "desc", RecurrenceFrequency.DAILY, LocalDate.now(), null, null, LocalDate.now(), 0, true, null
        );

        RecurringTemplate template = RecurringTemplate.builder().userId(1L).amount(BigDecimal.TEN).frequency(RecurrenceFrequency.DAILY).startDate(LocalDate.now()).nextOccurrence(LocalDate.now()).isActive(true).build();
        when(recurrencyTemplateMapper.mapToResponse(any())).thenReturn(
                new RecurringTemplateResponse(
                        1L,
                        BigDecimal.TEN,
                        null, // TransactionType
                        null, // Category
                        null, // PaymentMethod
                        "desc",
                        RecurrenceFrequency.DAILY,
                        LocalDate.now(), // startDate
                        null, // endDate
                        null, // maxOccurrences
                        LocalDate.now(), // nextOccurrence
                        0, // timesProcessed
                        true, // isActive
                        List.of() // transactions
                ));

        RecurringTemplateResponse resp = service.createRecurringFromDTO(dto);
        assertNotNull(resp);
    }
}
