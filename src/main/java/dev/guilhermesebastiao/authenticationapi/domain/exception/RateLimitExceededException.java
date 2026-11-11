package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RateLimitExceededException extends DomainException {
    private final long retryAfterSeconds;

    public RateLimitExceededException(long retryAfterSeconds) {
        super(
                "Limite de requisições excedido",
                "O limite de requisições foi excedido.",
                429,
                "RateLimitExceededException: The request rate limit has been exceeded.",
                DomainErrorCode.RATE_LIMIT_EXCEEDED,
                null,
                null
        );
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
