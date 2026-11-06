package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.fields_match.FieldsMatch;
import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;


@FieldsMatch(field = "newPassword", fieldMatch = "confirmPassword")
public record ChangePasswordRequest(
        @NotBlank(message = REQUIRED)
        @Length(min = 6, message = INVALID_MIN_SIZE)
        @Length(max = 20, message = INVALID_MAX_SIZE)
        @ValidPassword
        String currentPassword,

        @NotBlank(message = REQUIRED)
        @Length(min = 6, message = INVALID_MIN_SIZE)
        @Length(max = 20, message = INVALID_MAX_SIZE)
        @ValidPassword
        String newPassword,

        @NotBlank(message = REQUIRED)
        @Length(min = 6, message = INVALID_MIN_SIZE)
        @Length(max = 20, message = INVALID_MAX_SIZE)
        @ValidPassword
        String confirmPassword
) {
}
