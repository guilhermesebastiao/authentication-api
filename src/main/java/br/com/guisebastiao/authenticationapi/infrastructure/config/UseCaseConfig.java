package br.com.guisebastiao.authenticationapi.infrastructure.config;

import br.com.guisebastiao.authenticationapi.application.port.in.*;
import br.com.guisebastiao.authenticationapi.application.port.out.*;
import br.com.guisebastiao.authenticationapi.application.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class UseCaseConfig {

    @Bean
    public AccountActivateService accountActivateService(
            AccountActivationRepositoryPort accountActivationRepository,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher
    ) {
        return new AccountActivateService(
                accountActivationRepository,
                accountRepository,
                passwordEncoder,
                secureHasher
        );
    }

    @Bean
    public ChangePasswordService changePasswordService(
            AccountRepositoryPort accountRepository,
            SignOutAllUseCase signOutAllUseCase,
            PasswordEncoderPort passwordEncoder
    ) {
        return new ChangePasswordService(
                accountRepository,
                signOutAllUseCase,
                passwordEncoder
        );
    }

    @Bean
    public CleanupAccountActivationService cleanupAccountActivationService(
            AccountActivationRepositoryPort accountActivationRepository,
            LoggerPort logger
    ) {
        return new CleanupAccountActivationService(
                accountActivationRepository,
                logger
        );
    }

    @Bean
    public CleanupRecoverPasswordService cleanupRecoverPasswordService(
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            LoggerPort logger
    ) {
        return new CleanupRecoverPasswordService(
                recoverPasswordRepository,
                logger
        );
    }

    @Bean
    public CleanupSessionService cleanupSessionService(
            SessionRepositoryPort sessionRepository,
            LoggerPort logger
    ) {
        return new CleanupSessionService(
                sessionRepository,
                logger
        );
    }

    @Bean
    public CreateAccountActivationService createAccountActivationService(
            SendAccountActivationEmailUseCase sendAccountActivationEmail,
            AccountActivationRepositoryPort accountActivationRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        return new CreateAccountActivationService(
                sendAccountActivationEmail,
                accountActivationRepository,
                secureRandomGenerator,
                passwordEncoder,
                secureHasher,
                secureRandom
        );
    }

    @Bean
    public CreateAccountService createAccountService(
            CreateAccountActivationUseCase createAccountActivation,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            RoleRepositoryPort roleRepository
    ) {
        return new CreateAccountService(
                createAccountActivation,
                accountRepository,
                passwordEncoder,
                roleRepository
        );
    }

    @Bean
    public CreateRecoverPasswordService createRecoverPasswordService(
            SendRecoverPasswordEmailUseCase sendRecoverPasswordEmail,
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        return new CreateRecoverPasswordService(
                sendRecoverPasswordEmail,
                recoverPasswordRepository,
                secureRandomGenerator,
                accountRepository,
                passwordEncoder,
                secureHasher,
                secureRandom
        );
    }

    @Bean
    public CreateRefreshService createRefreshService(
            SecureRandomGeneratorPort secureRandomGenerator,
            RefreshRepositoryPort refreshRepository,
            SecureHasherPort secureHasher
    ) {
        return new CreateRefreshService(secureRandomGenerator, refreshRepository, secureHasher);
    }

    @Bean
    public CreateSessionService createSessionService(
            SecureRandomGeneratorPort secureRandomGenerator,
            SessionRepositoryPort sessionRepository,
            IpGeolocationPort ipGeolocation,
            SecureHasherPort secureHasher
    ) {
        return new CreateSessionService(secureRandomGenerator, sessionRepository, ipGeolocation, secureHasher);
    }

    @Bean
    public DisableAccountService disableAccountService(
            AccountRepositoryPort accountRepository,
            SignOutAllUseCase signOutAll,
            PasswordEncoderPort passwordEncoder
    ) {
        return new DisableAccountService(accountRepository, signOutAll, passwordEncoder);
    }

    @Bean
    public GetAccountActivationService getAccountActivationService(
            AccountActivationRepositoryPort accountActivationRepository,
            SecureHasherPort secureHasher
    ) {
        return new GetAccountActivationService(accountActivationRepository, secureHasher);
    }

    @Bean
    public GetCurrentAccountService getCurrentAccountService() {
        return new GetCurrentAccountService();
    }

    @Bean
    public GetCurrentSessionService getCurrentSessionService(
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        return new GetCurrentSessionService(sessionRepository, secureHasher);
    }

    @Bean
    public GetSessionsService getSessionsService(
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        return new GetSessionsService(sessionRepository, secureHasher);
    }

    @Bean
    public GoogleSignInService googleSignUpService(
            CreateAccountActivationUseCase createAccountActivationUseCase,
            GoogleAuthorizationPort googleAuthorization,
            CreateSessionUseCase createSession,
            CreateRefreshUseCase createRefresh,
            AccountRepositoryPort accountRepository,
            JwtTokenPort jwtToken
    ) {
        return new GoogleSignInService(
                createAccountActivationUseCase,
                googleAuthorization,
                createSession,
                createRefresh,
                accountRepository,
                jwtToken
        );
    }

    @Bean
    public RefreshTokenService refreshTokenService(
            ValidateSessionUseCase validateSession,
            CreateRefreshUseCase createRefresh,
            RefreshRepositoryPort refreshRepository,
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher,
            JwtTokenPort jwtToken
    ) {
        return new RefreshTokenService(
                validateSession,
                createRefresh,
                refreshRepository,
                sessionRepository,
                secureHasher,
                jwtToken
        );
    }

    @Bean
    public ResendAccountActivationEmailService resendAccountActivationEmailService(
            SendAccountActivationEmailUseCase sendAccountActivationEmail,
            AccountActivationRepositoryPort accountActivationRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        return new ResendAccountActivationEmailService(
                sendAccountActivationEmail,
                accountActivationRepository,
                passwordEncoder,
                secureHasher,
                secureRandom
        );
    }

    @Bean
    public ResendRecoverPasswordEmailService resendRecoverPasswordEmailService(
            SendRecoverPasswordEmailUseCase sendRecoverPasswordEmail,
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        return new ResendRecoverPasswordEmailService(
                sendRecoverPasswordEmail,
                recoverPasswordRepository,
                passwordEncoder,
                secureHasher,
                secureRandom
        );
    }

    @Bean
    public ResetPasswordService resetPasswordService(
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SignOutAllUseCase signOutAll,
            SecureHasherPort secureHasher
    ) {
        return new ResetPasswordService(
                recoverPasswordRepository,
                accountRepository,
                signOutAll,
                passwordEncoder,
                secureHasher
        );
    }

    @Bean
    public SendAccountActivationEmailService sendAccountActivationEmailService(EmailSenderPort emailSender) {
        return new SendAccountActivationEmailService(emailSender);
    }

    @Bean
    public SendRecoverPasswordEmailService sendRecoverPasswordEmailService(EmailSenderPort emailSender) {
        return new SendRecoverPasswordEmailService(emailSender);
    }

    @Bean
    public SessionSignOutService sessionSignOutService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository
    ) {
        return new SessionSignOutService(sessionRepository, refreshRepository);
    }

    @Bean
    public SignInService signInService(
            CreateAccountActivationUseCase createAccountActivationUseCase,
            AccountRepositoryPort accountRepository,
            CreateSessionUseCase createSession,
            CreateRefreshUseCase createRefresh,
            AuthenticationPort authentication,
            JwtTokenPort jwtToken
    ) {
        return new SignInService(
                createAccountActivationUseCase,
                createSession,
                createRefresh,
                accountRepository,
                authentication,
                jwtToken
        );
    }

    @Bean
    public SignOutAllService signOutAllService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository
    ) {
        return new SignOutAllService(sessionRepository, refreshRepository);
    }

    @Bean
    public SignOutService signOutService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository,
            SecureHasherPort secureHasher
    ) {
        return new SignOutService(sessionRepository, refreshRepository, secureHasher);
    }

    @Bean
    public ValidateRecoverPasswordService validateRecoverPasswordService(
            RecoverPasswordRepositoryPort recoverPasswordRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher
    ) {
        return new ValidateRecoverPasswordService(
                recoverPasswordRepository,
                passwordEncoder,
                secureHasher
        );
    }

    @Bean
    public ValidateSessionService validateSessionService(
            SessionRepositoryPort sessionRepository,
            SecureHasherPort secureHasher
    ) {
        return new ValidateSessionService(sessionRepository, secureHasher);
    }
}
