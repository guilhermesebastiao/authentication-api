package dev.guilhermesebastiao.authenticationapi.adapter.in.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ValidationErrorCode {
    public static final String REQUIRED = "REQUIRED";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String INVALID_MIN_SIZE = "INVALID_MIN_SIZE";
    public static final String INVALID_MAX_SIZE = "INVALID_MAX_SIZE";
    public static final String INVALID_OTP = "INVALID_OTP";
    public static final String INVALID_MATCH = "INVALID_MATCH";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String PASSWORD_MISSING_UPPERCASE = "PASSWORD_MISSING_UPPERCASE";
    public static final String PASSWORD_MISSING_LOWERCASE = "PASSWORD_MISSING_LOWERCASE";
    public static final String PASSWORD_REQUIRES_TWO_DIGITS = "PASSWORD_REQUIRES_TWO_DIGITS";
    public static final String PASSWORD_MISSING_SPECIAL_CHARACTER = "PASSWORD_MISSING_SPECIAL_CHARACTER";
    public static final String INVALID_MAX_NUMBER = "INVALID_MAX_NUMBER";
    public static final String INVALID_MIN_NUMBER = "INVALID_MIN_NUMBER";
}
