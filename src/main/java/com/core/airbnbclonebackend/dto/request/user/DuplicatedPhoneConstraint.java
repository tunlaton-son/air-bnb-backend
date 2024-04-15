package com.core.airbnbclonebackend.dto.request.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = DuplicatedPhoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedPhoneConstraint {

    String message() default "This Phone number is taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
