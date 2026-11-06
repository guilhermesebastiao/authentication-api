package dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.INVALID_PASSWORD;

@Documented
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default INVALID_PASSWORD;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
