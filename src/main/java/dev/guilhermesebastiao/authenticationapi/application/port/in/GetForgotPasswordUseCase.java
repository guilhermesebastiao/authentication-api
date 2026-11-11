package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.ForgotPasswordOutput;

public interface GetForgotPasswordUseCase {
    ForgotPasswordOutput execute(String forgotToken);
}
