package com.pvil.otuscourse.bookstockwebflux.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<IsbnConstraint, String> {
    @Override
    public void initialize(IsbnConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.matches("[0-9]+X?") && (s.length() == 9);
    }
}
