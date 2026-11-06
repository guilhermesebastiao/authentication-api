package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.result.JwtValidationResult;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;

public interface JwtTokenPort {
    String generate(Account account, String sessionToken);

    JwtValidationResult validate(String accessToken, String sessionToken);
}
