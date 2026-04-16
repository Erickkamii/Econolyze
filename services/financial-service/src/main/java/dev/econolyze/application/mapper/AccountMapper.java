package dev.econolyze.application.mapper;

import dev.econolyze.application.dto.request.CreateAccountRequest;
import dev.econolyze.application.dto.request.UpdateAccountRequest;
import dev.econolyze.application.dto.response.AccountResponse;
import dev.econolyze.application.dto.AccountDTO;
import dev.econolyze.domain.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface AccountMapper {
    Account mapToEntity(CreateAccountRequest request);
    Account mapToEntity(UpdateAccountRequest request);
    AccountResponse mapToResponse(Account account);
    AccountDTO mapToDTO(Account account);
}
