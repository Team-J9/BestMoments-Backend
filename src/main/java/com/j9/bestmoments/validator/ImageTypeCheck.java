package com.j9.bestmoments.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageTypeCheck {
    String message() default "Invalid Image File Type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}