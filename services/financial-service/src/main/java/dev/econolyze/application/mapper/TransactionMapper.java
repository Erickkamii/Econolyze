package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.application.dto.request.TransactionRequest;
import dev.econolyze.application.dto.response.TransactionResponse;
import dev.econolyze.domain.entity.Transaction;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;

@Mapper(componentModel = "cdi")
public interface TransactionMapper {

    Transaction mapToEntity(TransactionDTO transactionDTO);
    TransactionDTO mapToDTO(Transaction transaction);
    TransactionDTO mapToDTO(TransactionRequest transactionRequest);
    TransactionResponse mapToResponse(Transaction transaction);

    @AfterMapping
    default void ensurePaymentsInitialized(@MappingTarget Transaction transaction) {
        if (transaction.getPayments() == null) {
            transaction.setPayments(new ArrayList<>());
        }
    }
}
