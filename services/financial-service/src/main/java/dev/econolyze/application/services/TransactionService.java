package dev.econolyze.application.services;

import dev.econolyze.application.dto.PagedResponse;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.TransactionRequest;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.application.mapper.BalanceMapper;
import dev.econolyze.application.mapper.TransactionMapper;
import dev.econolyze.application.security.UserContext;
import dev.econolyze.domain.entity.Balance;
import dev.econolyze.domain.entity.Transaction;
import dev.econolyze.domain.enums.Category;
import dev.econolyze.domain.enums.TransactionType;
import dev.econolyze.infrastructure.repository.BalanceRepository;
import dev.econolyze.infrastructure.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;
    @Inject
    BalanceRepository balanceRepository;
    @Inject
    BalanceService balanceService;
    @Inject
    TransactionMapper transactionMapper;
    @Inject
    BalanceMapper balanceMapper;
    @Inject
    UserContext userContext;

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
        balanceService.updateUserBalance(transactionDTO, transaction.getUserId());
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

    void persistTransaction(TransactionDTO transactionDTO, Balance balance){
        if(transactionDTO.getType().isIncreaseBalance()){
            balance.setIncome(balance.getIncome().add(transactionDTO.getAmount()));
            balance.setBalance(balance.getBalance().add(transactionDTO.getAmount()));
        } else if(transactionDTO.getType().isDecreaseBalance()){
            balance.setExpenses(balance.getExpenses().add(transactionDTO.getAmount()));
            balance.setBalance(balance.getBalance().subtract(transactionDTO.getAmount()));
        }
        balanceService.setBalanceDifference(balanceMapper.mapToDTO(balance));
        balanceRepository.persist(balance);
        balanceMapper.mapToDTO(balance);
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

    Balance newBalance(Long userId){
        return Balance.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .date(LocalDate.now())
                .income(BigDecimal.ZERO)
                .expenses(BigDecimal.ZERO)
                .build();
    }

    public TransactionResponse getTransactionById(Long id) {
        return transactionMapper.mapToResponse(transactionRepository.findById(id));
    }
}
