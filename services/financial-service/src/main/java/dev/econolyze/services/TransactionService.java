package dev.econolyze.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.econolyze.dto.BalanceDTO;
import dev.econolyze.dto.TransactionDTO;
import dev.econolyze.entity.Transaction;
import dev.econolyze.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;
    @Inject
    ObjectMapper objectMapper;

    public List<TransactionDTO> getAllTransactionsByUserId(Long userId) {
        return transactionRepository.findAllTransactionsByUserId(userId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = objectMapper.convertValue(transactionDTO, Transaction.class);
        transactionRepository.persist(transaction);
        updateUserBalance(transactionDTO, transaction.getUserId());
        return mapToDTO(transaction);
    }

    private BalanceDTO updateUserBalance(TransactionDTO transactionDTO, Long userId) {
        return null;
    }

    public TransactionDTO mapToDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType().toString())
                .category(t.getCategory().toString())
                .date(t.getDate())
                .description(t.getDescription())
                .userId(t.getUserId())
                .method(t.getMethod().toString())
                .build();
    }
}
