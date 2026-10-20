package br.com.guisebastiao.authenticationapi.application.result;

import java.util.Set;
import java.util.UUID;

public record AccountResult(
        UUID id,
        String email,
        Set<String> roles
) {
}
