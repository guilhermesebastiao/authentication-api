package dev.guilhermesebastiao.authenticationapi.infrastructure.config;

import dev.guilhermesebastiao.authenticationapi.application.port.in.*;
import dev.guilhermesebastiao.authenticationapi.application.port.out.*;
import dev.guilhermesebastiao.authenticationapi.application.service.*;
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
            RevokeAllSessionsUseCase revokeAllSessionsUseCase,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        return new ChangePasswordService(
                revokeAllSessionsUseCase,
                accountRepository,
                passwordEncoder
        );
    }

    @Bean
    public CreateAccountActivationService createAccountActivationService(
            SendAccountActivationUseCase sendAccountActivationEmail,
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
    public ForgotPasswordService createRecoverPasswordService(
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            SendForgotUseCase sendRecoverPasswordEmail,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        return new ForgotPasswordService(
                forgotPasswordRepository,
                secureRandomGenerator,
                accountRepository,
                sendRecoverPasswordEmail,
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
            RevokeAllSessionsUseCase signOutAll,
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
    public GetRecoveryPasswordService getRecoverPasswordService(
            RecoveryPasswordRepositoryPort recoveryPasswordRepository,
            SecureHasherPort secureHasher
    ) {
        return new GetRecoveryPasswordService(
                recoveryPasswordRepository,
                secureHasher
        );
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
            SendAccountActivationUseCase sendAccountActivationEmail,
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
    public ResendForgotPasswordService resendRecoverPasswordEmailService(
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SendForgotUseCase sendRecoverPasswordEmail,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher,
            SecureRandom secureRandom
    ) {
        return new ResendForgotPasswordService(
                forgotPasswordRepository,
                sendRecoverPasswordEmail,
                passwordEncoder,
                secureHasher,
                secureRandom
        );
    }

    @Bean
    public ResetPasswordService resetPasswordService(
            RecoveryPasswordRepositoryPort recoveryPasswordRepository,
            AccountRepositoryPort accountRepository,
            PasswordEncoderPort passwordEncoder,
            RevokeAllSessionsUseCase signOutAll,
            SecureHasherPort secureHasher
    ) {
        return new ResetPasswordService(
                recoveryPasswordRepository,
                accountRepository,
                signOutAll,
                passwordEncoder,
                secureHasher
        );
    }

    @Bean
    public SendAccountActivationService sendAccountActivationEmailService(
            EmailSenderPort emailSender
    ) {
        return new SendAccountActivationService(emailSender);
    }

    @Bean
    public SendForgotService sendRecoverPasswordEmailService(
            EmailSenderPort emailSender
    ) {
        return new SendForgotService(emailSender);
    }

    @Bean
    public RevokeSessionsService revokeSessionsService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository
    ) {
        return new RevokeSessionsService(sessionRepository, refreshRepository);
    }

    @Bean
    public RevokeSessionService sessionSignOutService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository,
            SecureHasherPort secureHasher
    ) {
        return new RevokeSessionService(
                sessionRepository,
                refreshRepository,
                secureHasher
        );
    }

    @Bean
    public RevokeAllSessionsService revokeAllSessionsService(
            SessionRepositoryPort sessionRepository,
            RefreshRepositoryPort refreshRepository
    ) {
        return new RevokeAllSessionsService(
                sessionRepository,
                refreshRepository
        );
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
    public ValidateForgotPasswordService validateRecoverPasswordService(
            RecoveryPasswordRepositoryPort recoveryPasswordRepository,
            ForgotPasswordRepositoryPort forgotPasswordRepository,
            SecureRandomGeneratorPort secureRandomGenerator,
            PasswordEncoderPort passwordEncoder,
            SecureHasherPort secureHasher
    ) {
        return new ValidateForgotPasswordService(
                recoveryPasswordRepository,
                forgotPasswordRepository,
                secureRandomGenerator,
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
