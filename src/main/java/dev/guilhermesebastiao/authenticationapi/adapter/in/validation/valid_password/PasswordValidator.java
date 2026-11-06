package dev.guilhermesebastiao.authenticationapi.adapter.in.validation.valid_password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static dev.guilhermesebastiao.authenticationapi.adapter.in.validation.ValidationErrorCode.*;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final String SPECIAL_CHARACTER_REGEX = ".*[!@#$%&*].*";
    private static final String TWO_DIGITS_REGEX = ".*\\d.*\\d.*";
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String LOWERCASE_REGEX = ".*[a-z].*";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) return true;

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        if (!password.matches(UPPERCASE_REGEX)) {
            addViolation(context, PASSWORD_MISSING_UPPERCASE);
            valid = false;
        }

        if (!password.matches(LOWERCASE_REGEX)) {
            addViolation(context, PASSWORD_MISSING_LOWERCASE);
            valid = false;
        }

        if (!password.matches(TWO_DIGITS_REGEX)) {
            addViolation(context, PASSWORD_REQUIRES_TWO_DIGITS);
            valid = false;
        }

        if (!password.matches(SPECIAL_CHARACTER_REGEX)) {
            addViolation(context, PASSWORD_MISSING_SPECIAL_CHARACTER);
            valid = false;
        }

        return valid;
    }

    private void addViolation(ConstraintValidatorContext context, String code) {
        context.buildConstraintViolationWithTemplate(code).addConstraintViolation();
    }
}