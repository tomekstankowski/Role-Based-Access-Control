package com.stankowski_strzelka.rbac.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateNotPastValidator implements ConstraintValidator<DateNotPast, LocalDateTime> {

    @Override
    public void initialize(DateNotPast notPast) {
    }

    @Override
    public boolean isValid(LocalDateTime dateTime,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (dateTime == null) {
            return false;
        }
        return dateTime.isAfter(LocalDateTime.now().minusMinutes(1));

    }
}
