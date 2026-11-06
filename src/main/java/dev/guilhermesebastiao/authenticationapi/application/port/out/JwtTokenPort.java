package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.JwtValidationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface JwtTokenPort {
    String generate(Account account, String sessionToken);

    JwtValidationOutput validate(String accessToken, String sessionToken);
}
