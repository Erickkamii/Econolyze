package dev.econolyze.application.services;

import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import dev.econolyze.infrastructure.repository.BalanceRepository;
import dev.econolyze.application.mapper.TransactionMapper;
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

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    BalanceRepository balanceRepository;
    @Mock
    BalanceService balanceService;
    @Mock
    TransactionMapper transactionMapper;
    @Mock
    BalanceMapper balanceMapper;
    @Mock
    UserContext userContext;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void newBalance_shouldCreateZeroBalance() {
        Balance balance = transactionService.newBalance(10L);
        assertNotNull(balance);
        assertEquals(10L, balance.getUserId());
        assertEquals(BigDecimal.ZERO, balance.getBalance());
    }

    @Test
    void persistTransaction_increaseAndDecrease() {
        Balance balance = Balance.builder().userId(1L).balance(BigDecimal.valueOf(100)).income(BigDecimal.ZERO).expenses(BigDecimal.ZERO).date(LocalDate.now()).build();
        TransactionDTO inc = new TransactionDTO();
        inc.setAmount(BigDecimal.valueOf(50));
        inc.setType(TransactionType.INCOME);

        transactionService.persistTransaction(inc, balance);
        assertEquals(BigDecimal.valueOf(150), balance.getBalance());
        assertEquals(BigDecimal.valueOf(50), balance.getIncome());

        TransactionDTO dec = new TransactionDTO();
        dec.setAmount(BigDecimal.valueOf(20));
        dec.setType(TransactionType.EXPENSE);

        transactionService.persistTransaction(dec, balance);
        assertEquals(BigDecimal.valueOf(130), balance.getBalance());
        assertEquals(BigDecimal.valueOf(20), balance.getExpenses());
    }
}
