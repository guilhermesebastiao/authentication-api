package dev.guilhermesebastiao.authenticationapi.infrastructure.transaction;

import br.com.guisebastiao.authenticationapi.application.port.in.*;
import br.com.guisebastiao.authenticationapi.application.service.*;
import dev.guilhermesebastiao.authenticationapi.application.port.in.*;
import dev.guilhermesebastiao.authenticationapi.application.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class TransactionConfiguration {

    @Bean("transactionTemplate")
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager transactionManager
    ) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean(name = "readOnlyTransactionTemplate")
    public TransactionTemplate readOnlyTransactionTemplate(
            PlatformTransactionManager transactionManager
    ) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setReadOnly(true);
        return transactionTemplate;
    }

    @Bean
    @Primary
    public AccountActivateUseCase transactionalAccountActivateUseCase(
            AccountActivateService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(AccountActivateUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public ChangePasswordUseCase transactionalChangePasswordUseCase(
            ChangePasswordService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(ChangePasswordUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CleanupAccountActivationUseCase transactionalCleanupAccountActivationUseCase(
            CleanupAccountActivationService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CleanupAccountActivationUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CleanupRecoverPasswordsUseCase transactionalCleanupRecoverPasswordsUseCase(
            CleanupRecoverPasswordService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CleanupRecoverPasswordsUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CleanupSessionUseCase transactionalCleanupSessionUseCase(
            CleanupSessionService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CleanupSessionUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CreateAccountActivationUseCase transactionalCreateAccountActivationUseCase(
            CreateAccountActivationService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CreateAccountActivationUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CreateAccountUseCase transactionalCreateAccountUseCase(
            CreateAccountService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CreateAccountUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CreateRecoverPasswordUseCase transactionalCreateRecoverPasswordUseCase(
            CreateRecoverPasswordService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CreateRecoverPasswordUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CreateRefreshUseCase transactionalCreateRefreshUseCase(
            CreateRefreshService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CreateRefreshUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public CreateSessionUseCase transactionalCreateSessionUseCase(
            CreateSessionService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(CreateSessionUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public DisableAccountUseCase transactionalDisableAccountUseCase(
            DisableAccountService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(DisableAccountUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public GetCurrentAccountUseCase transactionalGetCurrentAccountUseCase(
            GetCurrentAccountService delegate,
            @Qualifier("readOnlyTransactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(GetCurrentAccountUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public GetCurrentSessionUseCase transactionalGetCurrentSessionUseCase(
            GetCurrentSessionService delegate,
            @Qualifier("readOnlyTransactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(GetCurrentSessionUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public GetSessionsUseCase transactionalGetSessionsUseCase(
            GetSessionsService delegate,
            @Qualifier("readOnlyTransactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(GetSessionsUseCase.class, delegate, transactionTemplate);
    }

    @Bean
    @Primary
    public GoogleSignInUseCase transactionalGoogleSignUpUseCase(
            GoogleSignInService delegate,
            @Qualifier("transactionTemplate") TransactionTemplate transactionTemplate
    ) {
        return transactional(GoogleSignInUseCase.class, delegate, transactionTemplate);
    }

    private <T> T transactional(Class<T> useCaseType, T delegate, TransactionTemplate transactionTemplate) {
        if (!useCaseType.isInterface()) {
            throw new IllegalArgumentException("%s must be an interface".formatted(useCaseType.getName()));
        }

        return useCaseType.cast(Proxy.newProxyInstance(
                useCaseType.getClassLoader(),
                new Class<?>[]{useCaseType},
                (proxy, method, args) -> transactionTemplate.execute(status -> invoke(delegate, method, args))
        ));
    }

    private Object invoke(Object delegate, Method method, Object[] args) {
        try {
            return method.invoke(delegate, args);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();

            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }

            if (cause instanceof Error error) {
                throw error;
            }

            throw new IllegalStateException(cause);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException("Unable to invoke use case", exception);
        }
    }
}
