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
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
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

    @WithSession
    public Uni<PagedResponse<TransactionResponse>> getAllTransactionsByUserId(int page, int size, String sortBy, String sortDirection, String type, String category) {
        Long userId = userContext.getUserId();
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.Ascending : Sort.Direction.Descending;
        Sort sort = Sort.by(sortBy, direction);

        TransactionType typeEnum = null;
        Category categoryEnum = null;

        try{
            if(type != null && !type.isEmpty()) typeEnum = TransactionType.valueOf(type);
            if (category != null && !category.isEmpty()) categoryEnum = Category.valueOf(category);
        } catch (IllegalArgumentException e){
            throw new BadRequestException("Invalid type or category value");
        }
        final TransactionType finalType = typeEnum;
        final Category finalCategory = categoryEnum;

        Uni<List<Transaction>> listUni = transactionRepository.findFiltered(userId, sort, finalType, finalCategory, page, size);
        Uni<Long> countUni = transactionRepository.countFiltered(userId, finalType, finalCategory);
        return PagedResponse.from(listUni, countUni, page, size, transactionMapper::mapToResponse);
    }

    @WithTransaction
    public Uni<TransactionResponse> saveTransaction(TransactionRequest request) {
        TransactionDTO transactionDTO = transactionMapper.mapToDTO(request);
        transactionDTO.setUserId(userContext.getUserId());
        Transaction transaction = transactionMapper.mapToEntity(transactionDTO);

        Payment payment = request.hasInitialPayment()
                ? Payment.builder()
                  .accountId(request.accountId())
                  .transaction(transaction)
                  .amount(request.initialPayment())
                  .method(request.method())
                  .paidAt(LocalDate.now())
                  .status(PaymentStatus.COMPLETED)
                  .build()
                : Payment.builder()
                  .transaction(transaction)
                  .amount(request.amount())
                  .method(request.method())
                  .paidAt(LocalDate.now())
                  .accountId(request.accountId())
                  .status(PaymentStatus.COMPLETED)
                  .build();

        transaction.getPayments().add(payment);
        transaction.recalculateStatus();

        return transactionRepository.persist(transaction)
                .flatMap(s -> accountService.updateAccountBalance(payment)
                        .map(i -> transactionMapper.mapToResponse(s)));

    }

    @WithSession
    Uni<List<TransactionDTO>> getTransactionByUserIdAndType(Long userId, TransactionType type){
        return transactionRepository.findAllTransactionsByUserIdAndType(userId, type)
                .map(transactions -> transactions.stream()
                        .map(transactionMapper::mapToDTO)
                        .toList());
    }

    @WithSession
    public Uni<TransactionResponse> getTransactionById(Long id) {
        return transactionRepository.findTransactionById(id).map(transactionMapper::mapToResponse);
    }
}
