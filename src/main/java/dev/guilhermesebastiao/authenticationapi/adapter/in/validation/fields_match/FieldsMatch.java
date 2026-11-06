package dev.guilhermesebastiao.authenticationapi.adapter.in.validation.fields_match;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.INVALID_MATCH;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldsMatchValidator.class)
@Documented
public @interface FieldsMatch {
    String message() default INVALID_MATCH;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
    String fieldMatch();
}
