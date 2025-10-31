package dev.econolyze.application.mapper;

import dev.econolyze.application.converter.EnumConverter;
import dev.econolyze.application.dto.TransactionDTO;
import dev.econolyze.domain.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", uses = {EnumConverter.class})
public interface TransactionMapper {

    Transaction mapToEntity(TransactionDTO transactionDTO);
    TransactionDTO mapToDTO(Transaction transaction);

}
