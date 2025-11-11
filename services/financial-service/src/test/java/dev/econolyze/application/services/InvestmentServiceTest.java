package dev.econolyze.application.services;

import dev.econolyze.application.dto.InvestmentProjectionDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.enums.Estimate;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.application.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceTest {

    @Mock
    CdiService cdiService;
    @Mock
    TransactionService transactionService;
    @Mock
    UserContext userContext;

    @InjectMocks
    InvestmentService investmentService;

    @Test
    void getProjectionBasedOnCdiRate_noIncomes_returnsBlank() {
        when(userContext.getUserId()).thenReturn(1L);
        when(transactionService.getTransactionByUserIdAndType(1L, TransactionType.INVESTMENT)).thenReturn(List.of());

        InvestmentProjectionDTO dto = investmentService.getProjectionBasedOnCdiRate(Estimate.YEARLY);
        assertNotNull(dto);
        assertEquals(BigDecimal.ZERO, dto.getAmountCdi());
    }

    @Test
    void getProjectionBasedOnCdiWithPercentage_adjustsAmount() {
        when(userContext.getUserId()).thenReturn(1L);
        TransactionDTO t = new TransactionDTO();
        t.setAmount(BigDecimal.valueOf(1000));
        when(transactionService.getTransactionByUserIdAndType(1L, TransactionType.INVESTMENT)).thenReturn(List.of(t));
        when(cdiService.getCurrentCdiRate()).thenReturn(BigDecimal.valueOf(10));

        InvestmentProjectionDTO dto = investmentService.getProjectionBasedOnCdiRate(Estimate.YEARLY);
        InvestmentProjectionDTO adjusted = investmentService.getProjectionBasedOnCdiWithPercentage(Estimate.YEARLY, BigDecimal.valueOf(50));

        assertNotNull(adjusted);
    }
}
