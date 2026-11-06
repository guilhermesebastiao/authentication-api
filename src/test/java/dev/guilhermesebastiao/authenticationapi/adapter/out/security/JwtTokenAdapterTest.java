package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter.JwtTokenAdapter;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.SecretsProperties;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.JwtValidationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtTokenAdapterTest {

    @Mock
    private SecretsProperties properties;

    @InjectMocks
    private JwtTokenAdapter adapter;

    @Test
    @DisplayName("Should generate a JWT with the expected header and claims")
    void givenAccountAndSession_whenGenerate_thenCreateTokenWithExpectedClaims() throws Exception {
        UUID accountId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        Account account = new Account(
                accountId,
                Set.of(
                        new Role(UUID.fromString("22222222-2222-2222-2222-222222222222"), "ROLE_ADMIN", null, null, null),
                        new Role(UUID.fromString("33333333-3333-3333-3333-333333333333"), "ROLE_USER", null, null, null)
                ),
                "user@example.com",
                null,
                null,
                null,
                null,
                null
        );

        String sessionToken = "session-token";
        String secret = "01234567890123456789012345678901";

        given(properties.accessToken()).willReturn(secret);
        given(properties.issuer()).willReturn("authentication-api");

        String token = adapter.generate(account, sessionToken);

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        assertEquals(JWSAlgorithm.HS256, signedJWT.getHeader().getAlgorithm());
        assertEquals(JOSEObjectType.JWT, signedJWT.getHeader().getType());
        assertEquals("authentication-api", claims.getIssuer());
        assertEquals(accountId.toString(), claims.getSubject());
        assertEquals(sessionToken, claims.getStringClaim("session"));
        assertEquals(Set.of("ADMIN", "USER"), Set.copyOf(claims.getStringListClaim("roles")));
        assertNotNull(claims.getIssueTime());
        assertNotNull(claims.getExpirationTime());

        assertEquals(
                15 * 60 * 1000L,
                claims.getExpirationTime().getTime() - claims.getIssueTime().getTime()
        );

        assertNotNull(claims.getJWTID());
    }

    @Test
    @DisplayName("Should validate a signed token with matching session")
    void givenValidToken_whenValidate_thenReturnValidResult() {
        UUID accountId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        Account account = new Account(
                accountId,
                Set.of(),
                "user@example.com",
                null,
                null,
                null,
                null,
                null
        );

        String sessionToken = "session-token";

        given(properties.accessToken()).willReturn("01234567890123456789012345678901");
        given(properties.issuer()).willReturn("authentication-api");

        String token = adapter.generate(account, sessionToken);

        JwtValidationOutput result = adapter.validate(token, sessionToken);

        assertTrue(result.isValid());
        assertEquals(JwtValidationStatus.VALID, result.status());
        assertEquals(accountId, result.userId());
    }

    @Test
    @DisplayName("Should reject a token with an invalid algorithm")
    void givenTokenWithInvalidAlgorithm_whenValidate_thenReturnInvalidAlgorithm() {
        UUID accountId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        Account account = new Account(accountId, Set.of(), null, null, null, null, null, null);
        String secret = "01234567890123456789012345678901";

        given(properties.accessToken()).willReturn(secret);
        given(properties.issuer()).willReturn("authentication-api");

        String[] tokenParts = adapter.generate(account, "session-token").split("\\.", -1);

        String invalidAlgorithmHeader = Base64URL
                .encode("{\"alg\":\"RS256\",\"typ\":\"JWT\"}")
                .toString();

        JwtValidationOutput result = adapter.validate(
                invalidAlgorithmHeader + "." + tokenParts[1] + "." + tokenParts[2],
                "session-token"
        );

        assertFalse(result.isValid());
        assertEquals(JwtValidationStatus.INVALID_ALGORITHM, result.status());
        assertNull(result.userId());
    }

    @Test
    @DisplayName("Should reject a token with an invalid signature")
    void givenTokenWithInvalidSignature_whenValidate_thenReturnInvalidSignature() {
        UUID accountId = UUID.fromString("66666666-6666-6666-6666-666666666666");
        Account account = new Account(accountId, Set.of(), null, null, null, null, null, null);
        String secret = "01234567890123456789012345678901";

        given(properties.accessToken()).willReturn(secret);
        given(properties.issuer()).willReturn("authentication-api");

        String[] tokenParts = adapter.generate(account, "session-token").split("\\.", -1);
        String invalidSignature = Base64URL.encode("invalid-signature").toString();

        JwtValidationOutput result = adapter.validate(
                tokenParts[0] + "." + tokenParts[1] + "." + invalidSignature,
                "session-token"
        );

        assertFalse(result.isValid());
        assertEquals(JwtValidationStatus.INVALID_SIGNATURE, result.status());
        assertNull(result.userId());
    }

    @Test
    @DisplayName("Should reject a token with an invalid issuer")
    void givenTokenWithInvalidIssuer_whenValidate_thenReturnInvalidIssuer() {
        UUID accountId = UUID.fromString("77777777-7777-7777-7777-777777777777");
        Account account = new Account(accountId, Set.of(), null, null, null, null, null, null);
        String secret = "01234567890123456789012345678901";

        given(properties.accessToken()).willReturn(secret);
        given(properties.issuer()).willReturn("unexpected-issuer");
        String token = adapter.generate(account, "session-token");
        given(properties.issuer()).willReturn("authentication-api");

        JwtValidationOutput result = adapter.validate(token, "session-token");

        assertFalse(result.isValid());
        assertEquals(JwtValidationStatus.INVALID_ISSUER, result.status());
        assertNull(result.userId());
    }

    @Test
    @DisplayName("Should reject a token without an expiration claim")
    void givenTokenWithoutExpiration_whenValidate_thenReturnInvalid() throws JOSEException {
        String secret = "01234567890123456789012345678901";

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("authentication-api")
                .subject("88888888-8888-8888-8888-888888888888")
                .claim("session", "session-token")
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
                claims
        );

        signedJWT.sign(new MACSigner(secret.getBytes(StandardCharsets.UTF_8)));

        given(properties.accessToken()).willReturn(secret);
        given(properties.issuer()).willReturn("authentication-api");

        JwtValidationOutput result = adapter.validate(signedJWT.serialize(), "session-token");

        assertFalse(result.isValid());
        assertEquals(JwtValidationStatus.INVALID, result.status());
        assertNull(result.userId());
    }

    @Test
    @DisplayName("Should reject an expired token")
    void givenExpiredToken_whenValidate_thenReturnExpired() throws JOSEException {
        String secret = "01234567890123456789012345678901";

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("authentication-api")
                .subject("99999999-9999-9999-9999-999999999999")
                .expirationTime(new Date(System.currentTimeMillis() - 1_000))
                .claim("session", "session-token")
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),
                claims
        );

        signedJWT.sign(new MACSigner(secret.getBytes(StandardCharsets.UTF_8)));

        given(properties.accessToken()).willReturn(secret);
        given(properties.issuer()).willReturn("authentication-api");

        JwtValidationOutput result = adapter.validate(signedJWT.serialize(), "session-token");

        assertFalse(result.isValid());
        assertEquals(JwtValidationStatus.EXPIRED, result.status());
        assertNull(result.userId());
    }

    @Test
    @DisplayName("Should reject a token with a different session")
    void givenTokenWithDifferentSession_whenValidate_thenReturnInvalidIssuer() {
        UUID accountId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        Account account = new Account(accountId, Set.of(), null, null, null, null, null, null);

        given(properties.accessToken()).willReturn("01234567890123456789012345678901");
        given(properties.issuer()).willReturn("authentication-api");
        String token = adapter.generate(account, "stored-session");

        JwtValidationOutput result = adapter.validate(token, "different-session");

        assertFalse(result.isValid());
        assertEquals(JwtValidationStatus.INVALID_ISSUER, result.status());
        assertNull(result.userId());
    }

    @Test
    @DisplayName("Should translate an invalid token parsing error")
    void givenMalformedToken_whenValidate_thenThrowRuntimeException() {
        given(properties.accessToken()).willReturn("01234567890123456789012345678901");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> adapter.validate("not-a-jwt", "session-token")
        );

        assertTrue(exception.getCause() instanceof ParseException);
    }
}
