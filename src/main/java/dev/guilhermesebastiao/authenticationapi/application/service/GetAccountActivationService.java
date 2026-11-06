package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.port.in.GetAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.SecureHasherPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.AccountActivation;

import java.time.Instant;

public class GetAccountActivationService implements GetAccountActivationUseCase {
    private final AccountActivationRepositoryPort accountActivationRepository;
    private final SecureHasherPort secureHasher;

    public GetAccountActivationService(
            AccountActivationRepositoryPort accountActivationRepository,
            SecureHasherPort secureHasher
    ) {
        this.accountActivationRepository = accountActivationRepository;
        this.secureHasher = secureHasher;
    }

    @Override
    public AccountActivationOutput execute(String activationToken) {
        String activationTokenHash = secureHasher.hash(activationToken);

        AccountActivation accountActivation = accountActivationRepository
                .findByActivationTokenHash(activationTokenHash)
                .orElseThrow(AccountActivationNotFoundException::new);


        validateAccountActivation(accountActivation);

        return new AccountActivationOutput(
                accountActivation.getAccount().getEmail(),
                activationToken,
                accountActivation.getExpiresAt(),
                accountActivation.getResendAvailableAt()
        );
    }

    private void validateAccountActivation(AccountActivation accountActivation) {
        if (accountActivation.getActivatedAt() != null) {
            throw new AccountAlreadyActivatedException();
        }

        if (!accountActivation.getExpiresAt().isAfter(Instant.now())) {
            throw new AccountActivationExpiredException();
        }
    }
}
