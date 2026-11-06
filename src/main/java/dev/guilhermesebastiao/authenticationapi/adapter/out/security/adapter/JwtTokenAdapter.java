package dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter;

import dev.guilhermesebastiao.authenticationapi.application.port.out.JwtTokenPort;
import dev.guilhermesebastiao.authenticationapi.application.dto.output.JwtValidationOutput;
import dev.guilhermesebastiao.authenticationapi.domain.enums.JwtValidationStatus;
import dev.guilhermesebastiao.authenticationapi.domain.model.Account;
import dev.guilhermesebastiao.authenticationapi.infrastructure.properties.SecretsProperties;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements JwtTokenPort {
    private final static int ACCESS_TOKEN_EXPIRES_IN_MINUTES = 15;
    private final static String CLAIM_SESSION = "session";
    private final static String CLAIM_ROLES = "roles";

    private final SecretsProperties properties;

    @Override
    public String generate(Account account, String sessionToken) {
        try {
            Instant now = Instant.now();

            Date expiresAt = Date.from(now.plus(ACCESS_TOKEN_EXPIRES_IN_MINUTES, ChronoUnit.MINUTES));
            Date issueTime = Date.from(now);
            String jwtID = UUID.randomUUID().toString();

            String secret = properties.accessToken();

            Set<String> roles = account.getRoles().stream()
                    .map(role -> role.getName().replace("ROLE_", ""))
                    .collect(Collectors.toSet());

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(properties.issuer())
                    .subject(account.getId().toString())
                    .issueTime(issueTime)
                    .expirationTime(expiresAt)
                    .claim(CLAIM_ROLES, roles)
                    .claim(CLAIM_SESSION, sessionToken)
                    .jwtID(jwtID)
                    .build();

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();

            SignedJWT signedJwt = new SignedJWT(header, claims);

            signedJwt.sign(new MACSigner(secret.getBytes(StandardCharsets.UTF_8)));

            return signedJwt.serialize();
        } catch (JOSEException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public JwtValidationOutput validate(String accessToken, String sessionToken) {
        try {
            String secret = properties.accessToken();

            SignedJWT signedJwt = SignedJWT.parse(accessToken);

            if (!JWSAlgorithm.HS256.equals(signedJwt.getHeader().getAlgorithm())) {
                return JwtValidationOutput.invalid(JwtValidationStatus.INVALID_ALGORITHM);
            }

            JWSVerifier verifier = new MACVerifier(secret.getBytes(StandardCharsets.UTF_8));

            if (!signedJwt.verify(verifier)) {
                return JwtValidationOutput.invalid(JwtValidationStatus.INVALID_SIGNATURE);
            }

            JWTClaimsSet claims = signedJwt.getJWTClaimsSet();

            if (!properties.issuer().equals(claims.getIssuer())) {
                return JwtValidationOutput.invalid(JwtValidationStatus.INVALID_ISSUER);
            }

            Date expirationTime = claims.getExpirationTime();

            if (expirationTime == null) {
                return JwtValidationOutput.invalid(JwtValidationStatus.INVALID);
            }

            if (expirationTime.before(new Date())) {
                return JwtValidationOutput.invalid(JwtValidationStatus.EXPIRED);
            }

            String session = claims.getClaim(CLAIM_SESSION).toString();

            if (!sessionToken.equals(session)) {
                return JwtValidationOutput.invalid(JwtValidationStatus.INVALID_ISSUER);
            }

            return JwtValidationOutput.valid(UUID.fromString(claims.getSubject()));
        } catch (JOSEException | ParseException exception) {
            throw new RuntimeException(exception);
        }
    }
}
