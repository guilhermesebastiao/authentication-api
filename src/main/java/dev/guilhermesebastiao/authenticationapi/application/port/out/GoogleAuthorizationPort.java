package dev.guilhermesebastiao.authenticationapi.application.port.out;

import dev.guilhermesebastiao.authenticationapi.application.result.GoogleAuthorizationResult;

public interface GoogleAuthorizationPort {
    GoogleAuthorizationResult authorize(String credential);
}
