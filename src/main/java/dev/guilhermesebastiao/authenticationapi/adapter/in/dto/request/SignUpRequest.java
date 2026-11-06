package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request;

import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.fields_match.FieldsMatch;
import dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@FieldsMatch(field = "password", fieldMatch = "confirmPassword")
public record SignUpRequest(
        @NotBlank
        @Email
        @Length(max = 255)
        String email,

        @NotBlank
        @Length(min = 6)
        @Length(max = 20)
        @ValidPassword
        String password,

        @NotBlank
        @Length(min = 6)
        @Length(max = 20)
        @ValidPassword
        String confirmPassword
) {
}
