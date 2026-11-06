package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ResetPasswordInput;

public interface ResetPasswordUseCase {
    void execute(ResetPasswordInput input);
}
