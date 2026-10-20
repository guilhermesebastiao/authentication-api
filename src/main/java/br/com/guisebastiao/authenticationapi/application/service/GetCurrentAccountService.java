package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.in.GetCurrentAccountUseCase;
import br.com.guisebastiao.authenticationapi.application.result.AccountResult;
import br.com.guisebastiao.authenticationapi.domain.model.Account;

import java.util.Set;
import java.util.stream.Collectors;

public class GetCurrentAccountService implements GetCurrentAccountUseCase {

    @Override
    public AccountResult execute(Account account) {

        Set<String> roles = account.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return new AccountResult(account.getId(), account.getEmail(),  roles);
    }
}
