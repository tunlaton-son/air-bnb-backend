package com.core.airbnbclonebackend.service;

import com.core.airbnbclonebackend.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {

    String toToken(User user);

    Optional<String> getSubFromToken(String token);
}
