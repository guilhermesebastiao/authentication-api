package dev.guilhermesebastiao.authenticationapi.adapter.in.openapi.common;

import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        name = "FieldError",
        description = "Validation error associated with a specific field"
)
public abstract class BaseFieldError {

    @Schema(
            description = "Field that failed validation",
            example = "email"
    )
    private String field;

    @Schema(
            description = "Validation error code",
            example = ValidationErrorCode.INVALID_EMAIL,
            allowableValues = {
                    ValidationErrorCode.REQUIRED,
                    ValidationErrorCode.INVALID_EMAIL,
                    ValidationErrorCode.INVALID_MIN_SIZE,
                    ValidationErrorCode.INVALID_MAX_SIZE,
                    ValidationErrorCode.INVALID_OTP,
                    ValidationErrorCode.INVALID_MATCH,
                    ValidationErrorCode.INVALID_PASSWORD,
                    ValidationErrorCode.PASSWORD_MISSING_UPPERCASE,
                    ValidationErrorCode.PASSWORD_MISSING_LOWERCASE,
                    ValidationErrorCode.PASSWORD_REQUIRES_TWO_DIGITS,
                    ValidationErrorCode.PASSWORD_MISSING_SPECIAL_CHARACTER,
                    ValidationErrorCode.INVALID_MAX_NUMBER,
                    ValidationErrorCode.INVALID_MIN_NUMBER
            }
    )
    private String code;
}