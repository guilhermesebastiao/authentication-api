package br.com.guisebastiao.authenticationapi.application.service;

import br.com.guisebastiao.authenticationapi.application.port.in.GetAccountActivationUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.AccountActivationRepositoryPort;
import br.com.guisebastiao.authenticationapi.application.port.out.SecureHasherPort;
import br.com.guisebastiao.authenticationapi.application.result.AccountActivationResult;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountActivationExpiredException;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountActivationNotFoundException;
import br.com.guisebastiao.authenticationapi.domain.exception.AccountAlreadyActivatedException;
import br.com.guisebastiao.authenticationapi.domain.model.AccountActivation;

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
    public AccountActivationResult execute(String activationToken) {
        String activationTokenHash = secureHasher.hash(activationToken);

        AccountActivation accountActivation = accountActivationRepository
                .findByActivationTokenHash(activationTokenHash)
                .orElseThrow(AccountActivationNotFoundException::new);


        validateAccountActivation(accountActivation);

        return new AccountActivationResult(
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
