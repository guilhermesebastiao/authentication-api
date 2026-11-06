package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.fields_match.FieldsMatch;
import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@FieldsMatch(field = "newPassword", fieldMatch = "confirmPassword")
public record ResetPasswordRequest(
        @NotBlank
        @Length(max = 32)
        String recoveryToken,

        @NotBlank
        @Length(min = 6)
        @Length(max = 20)
        @ValidPassword
        String newPassword,

        @NotBlank
        @Length(min = 6)
        @Length(max = 20)
        @ValidPassword
        String confirmPassword
) {
}
