package br.com.guisebastiao.authenticationapi.adapter.out.security;

import br.com.guisebastiao.authenticationapi.application.result.GoogleAuthorizationResult;
import br.com.guisebastiao.authenticationapi.domain.exception.GoogleAuthenticationException;
import br.com.guisebastiao.authenticationapi.domain.exception.InvalidGoogleTokenException;
import br.com.guisebastiao.authenticationapi.infrastructure.properties.GoogleProperties;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

@ExtendWith(MockitoExtension.class)
class GoogleAuthenticationAdapterTest {

    @Mock
    private GoogleIdTokenVerifier verifier;

    @Mock
    private GoogleProperties properties;

    @Mock
    private HttpTransport httpTransport;

    @Mock
    private JsonFactory jsonFactory;

    @InjectMocks
    private GoogleAuthenticationAdapter adapter;

    @Test
    @DisplayName("Should return the email from the verified Google token")
    void givenValidCode_whenAuthorize_thenReturnGoogleEmail() throws Exception {
        String code = "google-code";
        GoogleIdToken token = mock(GoogleIdToken.class);
        GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload()
                .setEmail("user@example.com");

        given(tokenResponse.getIdToken()).willReturn("id-token");
        given(verifier.verify("id-token")).willReturn(token);
        given(token.getPayload()).willReturn(payload);

        try (MockedConstruction<GoogleAuthorizationCodeTokenRequest> ignored =
                     mockTokenExchange(tokenResponse)) {
            GoogleAuthorizationResult result = adapter.authorize(code);

            assertEquals("user@example.com", result.email());
        }

        then(verifier).should().verify("id-token");
    }

    @Test
    @DisplayName("Should reject an unverifiable Google token")
    void givenCodeWithNoVerifiedToken_whenAuthorize_thenThrowInvalidGoogleTokenException() throws Exception {
        GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);
        given(tokenResponse.getIdToken()).willReturn("id-token");
        given(verifier.verify("id-token")).willReturn(null);

        try (MockedConstruction<GoogleAuthorizationCodeTokenRequest> ignored =
                     mockTokenExchange(tokenResponse)) {
            assertThrows(
                    InvalidGoogleTokenException.class,
                    () -> adapter.authorize("invalid-code")
            );
        }
    }

    @Test
    @DisplayName("Should translate Google security failures")
    void givenSecurityFailure_whenAuthorizeCode_thenThrowGoogleAuthenticationException() throws Exception {
        GoogleTokenResponse tokenResponse = mock(GoogleTokenResponse.class);
        GeneralSecurityException cause = new GeneralSecurityException("Verification failed");
        given(tokenResponse.getIdToken()).willReturn("id-token");
        org.mockito.BDDMockito.willThrow(cause)
                .given(verifier)
                .verify("id-token");

        try (MockedConstruction<GoogleAuthorizationCodeTokenRequest> ignored =
                     mockTokenExchange(tokenResponse)) {
            GoogleAuthenticationException exception = assertThrows(
                    GoogleAuthenticationException.class,
                    () -> adapter.authorize("google-code")
            );

            assertSame(cause, exception.getCause());
        }
    }

    @Test
    @DisplayName("Should translate Google I/O failures")
    void givenIoFailure_whenAuthorizeCode_thenThrowGoogleAuthenticationException() throws Exception {
        IOException cause = new IOException("Google service unavailable");

        try (MockedConstruction<GoogleAuthorizationCodeTokenRequest> ignored =
                     mockConstruction(
                             GoogleAuthorizationCodeTokenRequest.class,
                             (request, context) -> org.mockito.BDDMockito.willThrow(cause)
                                     .given(request)
                                     .execute()
                     )) {
            GoogleAuthenticationException exception = assertThrows(
                    GoogleAuthenticationException.class,
                    () -> adapter.authorize("google-code")
            );

            assertSame(cause, exception.getCause());
        }
    }

    private MockedConstruction<GoogleAuthorizationCodeTokenRequest> mockTokenExchange(
            GoogleTokenResponse tokenResponse
    ) throws IOException {
        return mockConstruction(
                GoogleAuthorizationCodeTokenRequest.class,
                (request, context) -> given(request.execute()).willReturn(tokenResponse)
        );
    }
}
