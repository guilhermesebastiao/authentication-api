package dev.guilhermesebastiao.authenticationapi.adapter.in.exception;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiErrorBody;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.common.ApiFieldError;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.ErrorLoggerPayload;
import dev.guilhermesebastiao.authenticationapi.domain.exception.RateLimitExceededException;
import dev.guilhermesebastiao.authenticationapi.adapter.out.logger.payload.WarnLoggerPayload;
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
import java.util.List;

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
                exception.getLoggerDetails(),
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
                exception.getMessage(),
                exception.getStatus(),
                exception.getCode(),
                exception.getDetails()
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

        List<ApiFieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiFieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ApiErrorBody data = ApiErrorBody.of(
                "Erro de validação",
                "Alguns campos da solicitação não passaram na validação.",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                DomainErrorCode.VALIDATION_ERROR,
                fieldErrors
        );

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
                "Método não suportado",
                "O método HTTP não é suportado para este endpoint.",
                HttpStatus.METHOD_NOT_ALLOWED.value(),
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
                "Rota não encontrada",
                "O endpoint solicitado não existe.",
                HttpStatus.NOT_FOUND.value(),
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
                "Erro do interno servidor",
                "Ocorreu um erro interno inesperado no servidor.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                DomainErrorCode.INTERNAL_SERVER_ERROR
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(data);
    }
}
