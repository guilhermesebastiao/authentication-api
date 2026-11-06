package dev.guilhermesebastiao.authenticationapi.adapter.in.validation;

import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.ResetPasswordRequest;
import dev.guilhermesebastiao.authenticationapi.adapter.in.dto.request.SignUpRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordRequestValidationTest {
    private static Validator validator;
    private static AutoCloseable validatorFactory;

    @BeforeAll
    static void createValidator() {
        var factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validatorFactory = factory::close;
    }

    @AfterAll
    static void closeValidator() throws Exception {
        validatorFactory.close();
    }

    @Test
    void rejectsResetPasswordWhenConfirmationDiffers() {
        var request = new ResetPasswordRequest("recovery-token", "SecurePass12!", "DifferentPass12!");

        assertTrue(validator.validate(request).stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("confirmPassword")));
    }

    @Test
    void acceptsResetPasswordWhenConfirmationAndPasswordPolicyAreValid() {
        var request = new ResetPasswordRequest("recovery-token", "SecurePass12!", "SecurePass12!");

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsResetPasswordThatDoesNotMeetPasswordPolicy() {
        var invalidPasswords = new String[]{
                "weakpass",       // missing uppercase, digits and special character
                "WEAKPASS12!",    // missing lowercase
                "WeakPass1!",     // needs at least two digits
                "WeakPass12?"     // special character is outside the allowed set
        };

        for (String password : invalidPasswords) {
            var request = new ResetPasswordRequest("recovery-token", password, password);
            assertFalse(validator.validate(request).isEmpty(), "Should reject: " + password);
        }
    }

    @Test
    void rejectsSignupWhenPasswordConfirmationDiffers() {
        var request = new SignUpRequest("user@example.com", "SecurePass12!", "DifferentPass12!");

        assertTrue(validator.validate(request).stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("confirmPassword")));
    }
}
