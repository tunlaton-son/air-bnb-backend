package com.core.airbnbclonebackend.config.security;

import com.core.airbnbclonebackend.repository.UserRepository;
import com.core.airbnbclonebackend.service.JwtService;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;
    private final String header = "Authorization";

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        getTokenString(request.getHeader(header))
                .flatMap(token -> jwtService.getSubFromToken(token))
                .ifPresent(
                        id -> {
                            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                                userRepository
                                        .findById(UUID.fromString(id))
                                        .ifPresent(
                                                user -> {
                                                    UsernamePasswordAuthenticationToken authenticationToken =
                                                            new UsernamePasswordAuthenticationToken(
                                                                    user, null, Collections.emptyList());
                                                    authenticationToken.setDetails(
                                                            new WebAuthenticationDetailsSource().buildDetails(request));
                                                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                                                });
                            }
                        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenString(String header) {
        if (header == null) {
            return Optional.empty();
        } else {
            String[] split = header.split(" ");
            if (split.length < 2) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]);
            }
        }
    }


}

