package dev.guilhermesebastiao.authenticationapi.domain.exception;

import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;

public class RateLimitExceededException extends DomainException {
    private final long retryAfterSeconds;

    public RateLimitExceededException(long retryAfterSeconds) {
        super(
                "Rate Limit Exceeded",
                429,
                "The request rate limit has been exceeded.",
                "RateLimitExceededException: The request rate limit has been exceeded.",
                DomainErrorCode.RATE_LIMIT_EXCEEDED
        );
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
