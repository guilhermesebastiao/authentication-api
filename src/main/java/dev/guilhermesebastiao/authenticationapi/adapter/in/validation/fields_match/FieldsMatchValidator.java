package dev.guilhermesebastiao.authenticationapi.adapter.in.validation.fields_match;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        Object fieldValue = wrapper.getPropertyValue(field);
        Object fieldMatchValue = wrapper.getPropertyValue(fieldMatch);

        if (Objects.equals(fieldValue, fieldMatchValue)) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(fieldMatch)
                .addConstraintViolation();

        return false;
    }
}
