package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.fields_match.FieldsMatch;
import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;
import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.INVALID_MAX_SIZE;
import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.INVALID_MIN_SIZE;
import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.REQUIRED;

@FieldsMatch(field = "password", fieldMatch = "confirmPassword")
public record SignUpRequest(
        @NotBlank(message = REQUIRED)
        @Email(message = INVALID_EMAIL)
        @Length(max = 255, message = INVALID_MAX_SIZE)
        String email,

        @NotBlank(message = REQUIRED)
        @Length(min = 6, message = INVALID_MIN_SIZE)
        @Length(max = 20, message = INVALID_MAX_SIZE)
        @ValidPassword
        String password,

        @NotBlank(message = REQUIRED)
        @Length(min = 6, message = INVALID_MIN_SIZE)
        @Length(max = 20, message = INVALID_MAX_SIZE)
        @ValidPassword
        String confirmPassword
) {
}
