package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.application.port.out.GoogleAuthorizationPort;
import dev.guilhermesebastiao.authenticationapi.application.result.GoogleAuthorizationResult;
import dev.guilhermesebastiao.authenticationapi.domain.exception.GoogleAuthenticationException;
import dev.guilhermesebastiao.authenticationapi.domain.exception.InvalidGoogleTokenException;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.GoogleProperties;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@RequiredArgsConstructor
public class GoogleAuthenticationAdapter implements GoogleAuthorizationPort {
    private final GoogleIdTokenVerifier verifier;
    private final GoogleProperties properties;
    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;

    @Override
    public GoogleAuthorizationResult authorize(String code) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    httpTransport,
                    jsonFactory,
                    properties.clientId(),
                    properties.clientSecret(),
                    code,
                    "postmessage"
            ).execute();

            String idTokenValue = tokenResponse.getIdToken();

            if (idTokenValue == null) {
                throw new InvalidGoogleTokenException();
            }

            GoogleIdToken idToken = verifier.verify(idTokenValue);

            if (idToken == null) {
                throw new InvalidGoogleTokenException();
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            return new GoogleAuthorizationResult(payload.getEmail());

        } catch (GeneralSecurityException | IOException exception) {
            throw new GoogleAuthenticationException(exception);
        }
    }
}
