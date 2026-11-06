package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.GetCurrentAccountUseCase;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

import java.util.Set;
import java.util.stream.Collectors;

public class GetCurrentAccountService implements GetCurrentAccountUseCase {

    @Override
    public AccountOutput execute(Account account) {

        Set<String> roles = account.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return new AccountOutput(account.getId(), account.getEmail(),  roles);
    }
}
