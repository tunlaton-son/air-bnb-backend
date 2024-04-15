package com.core.airbnbclonebackend.dto.request.user;

import com.core.airbnbclonebackend.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class DuplicatedPhoneValidator implements ConstraintValidator<DuplicatedPhoneConstraint, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value == null || value.isEmpty()) || !userRepository.findByPhone(value).isPresent();
    }
}
