package com.franktranvantu.springboot3.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { BirthdateValidator.class })
public @interface Birthdate {
    String message() default "You must be at least {min} years old";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int min() default 0;
}
