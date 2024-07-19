package com.j9.bestmoments.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = VideoTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VideoTypeCheck {
    String message() default "Invalid Video File Type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}