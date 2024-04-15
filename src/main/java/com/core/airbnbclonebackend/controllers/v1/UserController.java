package com.core.airbnbclonebackend.controllers.v1;

import com.core.airbnbclonebackend.config.security.exception.InvalidAuthenticationException;
import com.core.airbnbclonebackend.dto.request.user.UserRequest;
import com.core.airbnbclonebackend.dto.response.UserResponse;
import com.core.airbnbclonebackend.dto.response.UserWithToken;
import com.core.airbnbclonebackend.entity.User;
import com.core.airbnbclonebackend.repository.UserRepository;
import com.core.airbnbclonebackend.service.JwtService;
import com.core.airbnbclonebackend.service.UserService;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = userService.createUser(userRequest);
        UserResponse userResponse = new UserResponse(user.getId()
                ,user.getEmail()
                ,user.getName()
                ,user.getPhone());

        return ResponseEntity.status(201)
                .body(userResponse(new UserWithToken(userResponse, jwtService.toToken(user))));
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam) {
        Optional<User> optional = userRepository.findByEmail(loginParam.getEmail());
        if (optional.isPresent()
                && passwordEncoder.matches(loginParam.getPassword(), optional.get().getPassword())) {
            UserResponse userResponse = new UserResponse(optional.get().getId()
                                                        ,optional.get().getEmail()
                                                        ,optional.get().getName()
                                                        ,optional.get().getPhone());
            return ResponseEntity.ok(
                    userResponse(new UserWithToken(userResponse, jwtService.toToken(optional.get()))));
        } else {
            throw new InvalidAuthenticationException();
        }
    }

    private Map<String, Object> userResponse(UserWithToken userWithToken) {
        return new HashMap<String, Object>() {
            {
                put("user", userWithToken);
            }
        };
    }
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class LoginParam {
    @NotBlank(message = "Email is required field")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Password is required field")
    private String password;
}
