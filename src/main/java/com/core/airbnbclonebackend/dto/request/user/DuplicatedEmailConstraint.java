package com.core.airbnbclonebackend.dto.request.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DuplicatedEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedEmailConstraint {

    String message() default "This Email is taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
