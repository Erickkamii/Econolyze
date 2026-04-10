package dev.econolyze.application.services;

import dev.econolyze.application.dto.request.PaymentRequest;
import dev.econolyze.application.dto.response.PaymentResponse;
import dev.econolyze.application.mapper.PaymentMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Payment;
import dev.econolyze.domain.enums.TransactionStatus;
import dev.econolyze.infrastructure.repository.PaymentRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

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
    @Inject
    AccountService accountService;

    @WithTransaction
    public Uni<PaymentResponse> createPayment(PaymentRequest request) {
        return transactionRepository.findTransactionById(request.transactionId())
                .onItem().ifNull().failWith(() -> new RuntimeException("Transaction not found"))
                .flatMap(transaction -> {
                    if (!transaction.getUserId().equals(userContext.getUserId()))
                        throw new SecurityException("You cannot add a payment to another user's transaction");
                    if (transaction.getStatus() == TransactionStatus.PAID)
                        throw new RuntimeException("Transaction is already paid");
                    if (request.amount().compareTo(transaction.getRemainingBalance()) > 0)
                        throw new RuntimeException("Payment amount exceeds remaining balance");

                    Payment payment = paymentMapper.mapToEntity(request);
                    payment.setTransaction(transaction);
                    transaction.getPayments().add(payment);
                    transaction.recalculateStatus();

                    return paymentRepository.persist(payment)
                            .flatMap(savedPayment -> transactionRepository.persist(transaction)
                                    .flatMap(ignored -> accountService.updateAccountBalance(savedPayment))
                                    .map(ignored -> {
                                        log.info("Payment created for transaction {} with amount {}",
                                                transaction.getId(), savedPayment.getAmount());
                                        return paymentMapper.mapToResponse(savedPayment);
                                    }));
                });
    }

    @WithSession
    public Uni<List<PaymentResponse>> getPaymentsByTransactionId(Long transactionId){
        return paymentRepository.findByTransactionId(transactionId)
                .map(payments -> payments.stream()
                        .map(paymentMapper::mapToResponse)
                        .collect(Collectors.toList()));
    }

    @WithSession
    public Uni<PaymentResponse> getPaymentById(Long id){
        return paymentRepository.findById(id)
                .map(paymentMapper::mapToResponse);
    }

}
