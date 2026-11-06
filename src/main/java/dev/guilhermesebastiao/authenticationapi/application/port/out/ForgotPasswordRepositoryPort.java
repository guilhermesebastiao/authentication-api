package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.domain.model.ForgotPassword;

import java.util.Optional;

public interface ForgotPasswordRepositoryPort {
    ForgotPassword save(ForgotPassword forgotPassword);

    Optional<ForgotPassword> findByForgotTokenHash(String forgotTokenHash);
}
