package dev.guilhermesebastiao.authenticationapi.application.port.in;

import dev.guilhermesebastiao.authenticationapi.application.dto.input.ValidateForgotPasswordInput;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.RecoveryPasswordOutput;

public interface ValidateForgotPasswordUseCase {
    RecoveryPasswordOutput execute(ValidateForgotPasswordInput input);
}
