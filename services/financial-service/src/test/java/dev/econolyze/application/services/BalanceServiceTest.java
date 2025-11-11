package dev.econolyze.application.services;

import dev.econolyze.application.dto.BalanceDTO;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.response.BalanceResponse;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.infrastructure.repository.BalanceRepository;
import dev.econolyze.application.mapper.BalanceMapper;
import dev.econolyze.application.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    BalanceRepository balanceRepository;
    @Mock
    TransactionService transactionService;
    @Mock
    BalanceMapper balanceMapper;
    @Mock
    UserContext userContext;

    @InjectMocks
    BalanceService balanceService;

    @Test
    void setBalanceDifference_shouldCalculateCorrectly() {
        BalanceDTO dto = mock(BalanceDTO.class);
        when(dto.getBalance()).thenReturn(new BigDecimal("1000"));
        when(dto.getIncome()).thenReturn(new BigDecimal("1500"));
        when(dto.getExpenses()).thenReturn(new BigDecimal("300"));

        BigDecimal diff = balanceService.setBalanceDifference(dto);

        // balance - (income - expenses) = 1000 - (1500 - 300) = 1000 - 1200 = -200
        assertEquals(new BigDecimal("-200"), diff);
    }

    @Test
    void updateUserBalance_whenBalanceExists_shouldPersistTransaction() {
        TransactionDTO transactionDTO = mock(TransactionDTO.class);
        Long userId = 1L;
        Balance balance = Balance.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .income(BigDecimal.ZERO)
                .expenses(BigDecimal.ZERO)
                .date(LocalDate.now())
                .build();

        when(balanceRepository.findById(userId)).thenReturn(balance);

        balanceService.updateUserBalance(transactionDTO, userId);

        verify(transactionService, times(1)).persistTransaction(transactionDTO, balance);
        // newBalance should not be called when balance exists
        verify(transactionService, never()).newBalance(anyLong());
    }

    @Test
    void updateUserBalance_whenBalanceNotExists_shouldCreateAndPersist() {
        TransactionDTO transactionDTO = mock(TransactionDTO.class);
        Long userId = 2L;

        when(balanceRepository.findById(userId)).thenReturn(null);

        Balance newBalance = Balance.builder().userId(userId).balance(BigDecimal.ZERO).income(BigDecimal.ZERO).expenses(BigDecimal.ZERO).date(LocalDate.now()).build();
        when(transactionService.newBalance(userId)).thenReturn(newBalance);

        balanceService.updateUserBalance(transactionDTO, userId);

        verify(transactionService, times(1)).newBalance(userId);
        verify(transactionService, times(1)).persistTransaction(transactionDTO, newBalance);
    }

    @Test
    void getBalanceByUserId_shouldReturnResponse() {
        Long userId = 3L;
        when(userContext.getUserId()).thenReturn(userId);

        Balance balance = Balance.builder()
                .userId(userId)
                .balance(new BigDecimal("500"))
                .income(new BigDecimal("700"))
                .expenses(new BigDecimal("200"))
                .date(LocalDate.now())
                .build();

        when(balanceRepository.findByUserIdForUpdate(userId)).thenReturn(balance);
        BalanceDTO dto = mock(BalanceDTO.class);
        when(balanceMapper.mapToDTO(balance)).thenReturn(dto);
        when(dto.getBalance()).thenReturn(balance.getBalance());
        when(dto.getIncome()).thenReturn(balance.getIncome());
        when(dto.getExpenses()).thenReturn(balance.getExpenses());

        BalanceResponse response = balanceService.getBalanceByUserId();

        assertNotNull(response);
        assertEquals(balance.getBalance(), response.balance());
    }
}
