package com.stankowski_strzelka.rbac.validator;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDateTime;

public class MaxDatesDifferenceValidator implements ConstraintValidator<MaxDatesDifference, Object> {
    private String firstDateName;
    private String secondDateName;
    private long millis;

    public void initialize(MaxDatesDifference constraintAnnotation) {
        firstDateName = constraintAnnotation.first();
        secondDateName = constraintAnnotation.second();
        millis = constraintAnnotation.millis();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (firstDateName.isEmpty()) {
            throw new IllegalArgumentException("'first' argument must not be empty");
        }
        if (secondDateName.isEmpty()) {
            throw new IllegalArgumentException("'second' argument must not be empty");
        }

        try {
            final Object firstObj = BeanUtils.getProperty(value, firstDateName);
            final Object secondObj = BeanUtils.getProperty(value, secondDateName);

            if (firstObj == null || secondObj == null) {
                return false;
            }
            final LocalDateTime firstDate = LocalDateTime.parse(firstObj.toString());
            final LocalDateTime secondDate = LocalDateTime.parse(secondObj.toString());
            final Duration duration = Duration.between(firstDate, secondDate);
            return duration.toMillis() <= millis;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassCastException ex1) {
            throw new IllegalArgumentException("Provided arguments are invalid");
        } catch (Exception ex2) {
            return false;
        }

    }
}
