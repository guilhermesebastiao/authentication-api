package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.command.GoogleSignInCommand;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.GoogleSignInUseCase;
import br.com.guisebastiao.authenticationapi.application.port.out.*;
import br.com.guisebastiao.authenticationapi.application.result.*;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.GoogleAuthorizationPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.result.*;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountDisabledException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotFoundException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public class GoogleSignInService implements GoogleSignInUseCase {
    private final CreateAccountActivationUseCase createAccountActivationUseCase;
    private final GoogleAuthorizationPort googleAuthorization;
    private final CreateSessionUseCase createSessionUseCase;
    private final CreateRefreshUseCase createRefreshUseCase;
    private final AccountRepositoryPort accountRepository;
    private final JwtTokenPort jwtToken;

    public GoogleSignInService(
            CreateAccountActivationUseCase createAccountActivationUseCase,
            GoogleAuthorizationPort googleAuthorization,
            CreateSessionUseCase createSessionUseCase,
            CreateRefreshUseCase createRefreshUseCase,
            AccountRepositoryPort accountRepository,
            JwtTokenPort jwtToken
    ) {
        this.createAccountActivationUseCase = createAccountActivationUseCase;
        this.createSessionUseCase = createSessionUseCase;
        this.createRefreshUseCase = createRefreshUseCase;
        this.googleAuthorization = googleAuthorization;
        this.accountRepository = accountRepository;
        this.jwtToken = jwtToken;
    }

    @Override
    public AuthResult execute(GoogleSignInCommand command, String userAgent, String ipAddress) {
        GoogleAuthorizationResult googleResult = googleAuthorization.authorize(command.code());

        Account account = accountRepository.findByEmail(googleResult.email())
                .orElseThrow(AccountNotFoundException::new);

        if (account.getStatus() == AccountStatus.PENDING) {
            AccountActivationResult data = createAccountActivationUseCase.execute(account);

            throw new AccountNotActivatedException(data);
        }

        if (account.getStatus() == AccountStatus.DISABLED) {
            throw new AccountDisabledException();
        }

        CreateSessionResult sessionResult = createSessionUseCase.execute(
                account,
                userAgent,
                ipAddress
        );

        CreateRefreshResult refreshResult = createRefreshUseCase.execute(sessionResult.session());

        String accessToken = jwtToken.generate(account, sessionResult.sessionToken());

        return new AuthResult(accessToken, refreshResult.refreshToken(), sessionResult.sessionToken());
    }
}
