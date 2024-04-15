package com.core.airbnbclonebackend.service;

import com.core.airbnbclonebackend.dto.request.user.UserRequest;
import com.core.airbnbclonebackend.entity.User;
import com.core.airbnbclonebackend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(@Valid UserRequest userRequest) {
        User user =
                new User(
                        userRequest.getEmail(),
                        userRequest.getName(),
                        userRequest.getPhone(),
                        passwordEncoder.encode(userRequest.getPassword())
                );
        userRepository.save(user);
        return user;
    }
}
