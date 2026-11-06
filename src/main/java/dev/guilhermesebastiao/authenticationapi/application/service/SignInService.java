package dev.guilhermesebastiao.authenticationapi.application.service;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.SignInInput;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateAccountActivationUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateRefreshUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.CreateSessionUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.in.SignInUseCase;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AccountRepositoryPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.AuthenticationPort;
import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AccountActivationOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.AuthOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateRefreshOutput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.CreateSessionOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.AccountStatus;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountInvalidCredentialsException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.AccountNotActivatedException;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public class SignInService implements SignInUseCase {
    private final CreateAccountActivationUseCase createAccountActivationUseCase;
    private final CreateSessionUseCase createSessionUseCase;
    private final CreateRefreshUseCase createRefreshUseCase;
    private final AccountRepositoryPort accountRepository;
    private final AuthenticationPort authenticationPort;
    private final JwtTokenPort jwtToken;

    public SignInService(
            CreateAccountActivationUseCase createAccountActivationUseCase,
            CreateSessionUseCase createSessionUseCase,
            CreateRefreshUseCase createRefreshUseCase,
            AccountRepositoryPort accountRepository,
            AuthenticationPort authenticationPort,
            JwtTokenPort jwtToken
    ) {
        this.createAccountActivationUseCase = createAccountActivationUseCase;
        this.createSessionUseCase = createSessionUseCase;
        this.createRefreshUseCase = createRefreshUseCase;
        this.authenticationPort = authenticationPort;
        this.accountRepository = accountRepository;
        this.jwtToken = jwtToken;
    }

    @Override
    public AuthOutput execute(SignInInput input, String userAgent, String ipAddress) {
        Account account = accountRepository.findByEmail(input.email())
                .orElseThrow(AccountInvalidCredentialsException::new);

        if (account.getStatus() == AccountStatus.PENDING) {
            AccountActivationOutput data = createAccountActivationUseCase.execute(account);

            throw new AccountNotActivatedException(data);
        }

        authenticationPort.authenticate(input.email(), input.password());

        CreateSessionOutput sessionResult = createSessionUseCase.execute(
                account,
                userAgent,
                ipAddress
        );

        CreateRefreshOutput refreshResult = createRefreshUseCase.execute(sessionResult.session());

        String accessToken = jwtToken.generate(account, sessionResult.sessionToken());

        return new AuthOutput(accessToken, refreshResult.refreshToken(), sessionResult.sessionToken());
    }
}
