package com.stankowski_strzelka.rbac.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = DateNotPastValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateNotPast {
    String message() default "Date cannot be past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
