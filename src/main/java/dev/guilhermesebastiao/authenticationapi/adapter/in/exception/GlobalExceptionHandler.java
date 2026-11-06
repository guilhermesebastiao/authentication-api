package dev.guilhermesebastiao.authenticationapi.adapter.in.exception;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiErrorBody;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.ErrorLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RateLimitExceededException;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.WarnLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.application.port.out.LoggerPort;
import dev.guilhermesebastiao.authenticationapi.domain.enums.DomainErrorCode;
import dev.guilhermesebastiao.authenticationapi.domain.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final LoggerPort logger;

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorBody> handleDomainException(
            HttpServletRequest request,
            HttpServletResponse response,
            DomainException exception
    ) {
        WarnLoggerPayload payload = new WarnLoggerPayload(
                Instant.now(),
                exception.getLogMessage(),
                request.getMethod(),
                request.getRequestURI(),
                exception.getStatus(),
                request.getRemoteAddr(),
                exception.getCode(),
                exception
        );

        logger.warn(payload);

        ApiErrorBody data = ApiErrorBody.of(
                exception.getTitle(),
                exception.getStatus(),
                exception.getDetail(),
                exception.getCode()
        );

        if (exception instanceof RateLimitExceededException rateLimitException) {
            response.setHeader("Retry-After", String.valueOf(rateLimitException.getRetryAfterSeconds()));
        }

        return ResponseEntity.status(exception.getStatus()).body(data);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorBody> handleMethodArgumentNotValidException(
            HttpServletRequest request,
            MethodArgumentNotValidException exception
    ) {
        WarnLoggerPayload payload = new WarnLoggerPayload(
                Instant.now(),
                "Request validation failed due to invalid field values",
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                request.getRemoteAddr(),
                DomainErrorCode.VALIDATION_ERROR,
                exception
        );

        logger.warn(payload);

        ApiErrorBody data = ApiErrorBody.of(
                "Validation Error",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "One or more request fields failed validation",
                DomainErrorCode.VALIDATION_ERROR);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(data);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorBody> handleMethodNotAllowed(
            HttpServletRequest request,
            HttpRequestMethodNotSupportedException exception
    ) {
        WarnLoggerPayload payload = new WarnLoggerPayload(
                Instant.now(),
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                request.getRemoteAddr(),
                DomainErrorCode.METHOD_NOT_ALLOWED,
                exception
        );

        logger.warn(payload);

        ApiErrorBody data = ApiErrorBody.of(
                "Method Not Supported",
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "The HTTP method is not supported for this endpoint",
                DomainErrorCode.METHOD_NOT_ALLOWED
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED.value()).body(data);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorBody> handleNotFound(
            HttpServletRequest request,
            NoHandlerFoundException exception
    ) {
        WarnLoggerPayload payload = new WarnLoggerPayload(
                Instant.now(),
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                request.getRemoteAddr(),
                DomainErrorCode.ROUTE_NOT_FOUND,
                exception
        );

        logger.warn(payload);

        ApiErrorBody data = ApiErrorBody.of(
                "Route Not Found",
                HttpStatus.NOT_FOUND.value(),
                "The requested endpoint does not exist",
                DomainErrorCode.ROUTE_NOT_FOUND
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(data);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorBody> handleRuntimeException(
            HttpServletRequest request,
            RuntimeException exception
    ) {
        ErrorLoggerPayload payload = new ErrorLoggerPayload(
                Instant.now(),
                exception.getMessage(),
                request.getMethod(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRemoteAddr(),
                DomainErrorCode.INTERNAL_SERVER_ERROR,
                exception
        );

        logger.error(payload);

        ApiErrorBody data = ApiErrorBody.of(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal server error occurred",
                DomainErrorCode.INTERNAL_SERVER_ERROR
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(data);
    }
}
