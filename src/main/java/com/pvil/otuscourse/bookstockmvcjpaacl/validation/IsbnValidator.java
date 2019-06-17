package com.pvil.otuscourse.bookstockmvcjpaacl.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<IsbnConstraint, String> {
    @Override
    public void initialize(IsbnConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && (s.matches("[0-9]{9}[0-9X]") || s.matches("(97[89])[0-9]{10}"));
    }
}
