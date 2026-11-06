package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.dto.output.GoogleAuthorizationOutput;

public interface GoogleAuthorizationPort {
    GoogleAuthorizationOutput authorize(String credential);
}
