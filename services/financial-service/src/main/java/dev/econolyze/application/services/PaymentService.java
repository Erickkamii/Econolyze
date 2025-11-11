package dev.econolyze.application.services;

import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.application.dto.response.PaymentResponse;
import dev.econolyze.application.mapper.PaymentMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Payment;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.infrastructure.repository.PaymentRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    @Inject
    PaymentRepository paymentRepository;
    @Inject
    TransactionRepository transactionRepository;
    @Inject
    PaymentMapper paymentMapper;
    @Inject
    UserContext userContext;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request){
        Transaction transaction = transactionRepository.findTransactionById(request.transactionId());
        if(transaction == null)
            throw new RuntimeException("Transaction not found");
        if (!transaction.getUserId().equals(userContext.getUserId())) {
            throw new SecurityException("You cannot add a payment to another user's transaction");
        }

        Payment payment = paymentMapper.mapToEntity(request);
        payment.setTransaction(transaction);
        transaction.getPayments().add(payment);
        paymentRepository.persist(payment);
        transaction.recalculateStatus();
        log.info("Payment created for transaction {} with amount {}", transaction.getId(), payment.getAmount());
        return paymentMapper.mapToResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByTransactionId(Long transactionId){
        return paymentRepository.findByTransactionId(transactionId).stream()
                .map(paymentMapper::mapToResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(Long id){
        return paymentMapper.mapToResponse(paymentRepository.findById(id));
    }

}
