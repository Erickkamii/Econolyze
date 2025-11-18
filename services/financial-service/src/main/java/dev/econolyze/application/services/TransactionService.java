package dev.econolyze.application.services;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.TransactionRequest;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.mapper.TransactionMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Payment;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.PaymentStatus;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;
    @Inject
    TransactionMapper transactionMapper;
    @Inject
    UserContext userContext;
    @Inject
    AccountService accountService;

    public PagedResponse<TransactionResponse> getAllTransactionsByUserId(int page, int pageSize) {
        Long userId = userContext.getUserId();
        return PagedResponse.fromPanacheQuery(
                transactionRepository.findPagedByUserId(userId, page, pageSize),
                page, pageSize,
                transactionMapper::mapToResponse
        );
    }

    @Transactional
    public TransactionResponse saveTransaction(TransactionRequest request) {
        TransactionDTO transactionDTO = transactionMapper.mapToDTO(request);
        transactionDTO.setUserId(userContext.getUserId());
        Transaction transaction = transactionMapper.mapToEntity(transactionDTO);
        if (request.hasInitialPayment()){
            Payment initialPayment = Payment.builder()
                    .accountId(request.accountId())
                    .transaction(transaction)
                    .amount(request.initialPayment())
                    .method(request.method())
                    .paidAt(LocalDate.now())
                    .status(PaymentStatus.COMPLETED)
                    .build();
            transaction.getPayments().add(initialPayment);
            accountService.updateAccountBalance(initialPayment);
        }else {
            Payment p = Payment.builder()
                    .transaction(transaction)
                    .amount(request.amount())
                    .method(request.method())
                    .paidAt(LocalDate.now())
                    .accountId(request.accountId())
                    .status(PaymentStatus.COMPLETED)
                    .build();

            transaction.getPayments().add(p);
            accountService.updateAccountBalance(p);
        }
        transaction.recalculateStatus();
        transactionRepository.persist(transaction);
        return transactionMapper.mapToResponse(transaction);
    }

    public PagedResponse<TransactionResponse> getAllTransactionsByUserIdAndType(TransactionType transactionType, int page, int pageSize) {
        Long userId = userContext.getUserId();
        return PagedResponse.fromPanacheQuery(
                transactionRepository.findPagedByUserIdAndType(userId, transactionType, page,pageSize),
                page,
                pageSize,
                transactionMapper::mapToResponse);
    }

    public PagedResponse<TransactionResponse> getTransactionByUserIdAndCategory(Category category, int page, int pageSize){
        Long userId = userContext.getUserId();
        return PagedResponse.fromPanacheQuery(transactionRepository.findPagedByUserIdAndCategory(userId, category, page, pageSize),
                page,
                pageSize,
                transactionMapper::mapToResponse);
    }

    List<TransactionDTO> getTransactionByUserIdAndType(Long userId, TransactionType type){
        return transactionRepository.findAllTransactionsByUserIdAndType(userId, type).stream()
                .map(transactionMapper::mapToDTO)
                .toList();
    }

    public TransactionResponse getTransactionById(Long id) {
        return transactionMapper.mapToResponse(transactionRepository.findById(id));
    }
}
