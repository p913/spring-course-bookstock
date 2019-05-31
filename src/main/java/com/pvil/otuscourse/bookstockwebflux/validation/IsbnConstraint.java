package com.pvil.otuscourse.bookstockwebflux.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsbnValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsbnConstraint {
    String message() default "ISBN должен состоять из 8 цифр и еще одной контрольной цифры или X ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
