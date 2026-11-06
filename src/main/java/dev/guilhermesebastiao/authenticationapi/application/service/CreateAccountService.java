package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.CreateAccountCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.*;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.PasswordEncoderPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.RoleRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.result.AccountActivationResult;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyExistsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RoleNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;

import java.util.Optional;
import java.util.Set;

public class CreateAccountService implements CreateAccountUseCase {
    private static final String DEFAULT_ROLE_NAME = "ROLE_USER";

    private final CreateAccountActivationUseCase createAccountActivationUseCase;
    private final AccountRepositoryPort accountRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final RoleRepositoryPort roleRepository;

    public CreateAccountService(
            CreateAccountActivationUseCase createAccountActivationUseCase,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            RoleRepositoryPort roleRepository
    ) {
        this.createAccountActivationUseCase = createAccountActivationUseCase;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public AccountActivationResult execute(CreateAccountCommand command) {
        Optional<Account> existsAccount = accountRepository.findByEmail(command.email());

        if (existsAccount.isPresent()) {
            throw new AccountAlreadyExistsException();
        }

        Account accountEntity = createAccount(command);

        Account account = accountRepository.save(accountEntity);

        return createAccountActivationUseCase.execute(account);
    }

    private Account createAccount(CreateAccountCommand command) {
        String passwordHash = passwordEncoder.hash(command.password());

        Account accountEntity = new Account();

        accountEntity.setEmail(command.email());
        accountEntity.setPasswordHash(passwordHash);
        accountEntity.setStatus(AccountStatus.PENDING);

        setDefaultRoleInAccount(accountEntity);

        return accountEntity;
    }

    private void setDefaultRoleInAccount(Account accountEntity) {
        Role role = roleRepository.findRoleByName(DEFAULT_ROLE_NAME)
                .orElseThrow(RoleNotFoundException::new);

        accountEntity.setRoles(Set.of(role));
    }
}
