package com.franktranvantu.springboot3.validator;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE)
public class BirthdateValidator implements ConstraintValidator<Birthdate, LocalDate> {
    int min;

    @Override
    public void initialize(Birthdate constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        final var years = ChronoUnit.YEARS.between(birthdate, LocalDate.now());
        return years >= min;
    }
}
