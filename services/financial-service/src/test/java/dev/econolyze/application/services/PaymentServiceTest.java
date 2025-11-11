package dev.econolyze.application.services;

import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.infrastructure.repository.PaymentRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import dev.econolyze.application.mapper.PaymentMapper;
import dev.econolyze.application.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    PaymentMapper paymentMapper;
    @Mock
    UserContext userContext;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void createPayment_whenTransactionNotFound_shouldThrow() {
        PaymentRequest req = new PaymentRequest(1L, BigDecimal.TEN, null, null);
        when(transactionRepository.findTransactionById(1L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> paymentService.createPayment(req));
    }

    @Test
    void createPayment_whenDifferentUser_shouldThrowSecurity() {
        Transaction tx = new Transaction();
        tx.setUserId(2L);
        when(transactionRepository.findTransactionById(1L)).thenReturn(tx);
        when(userContext.getUserId()).thenReturn(1L);
        PaymentRequest req = new PaymentRequest(1L, BigDecimal.TEN, null, null);
        assertThrows(SecurityException.class, () -> paymentService.createPayment(req));
    }
}
