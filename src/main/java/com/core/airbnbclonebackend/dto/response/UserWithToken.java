package com.core.airbnbclonebackend.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserWithToken {
    private String email;
    private String name;
    private String phone;
    private String token;

    public UserWithToken(UserResponse userResponse, String token) {
        this.email = userResponse.getEmail();
        this.name = userResponse.getName();
        this.phone = userResponse.getPhone();
        this.token = token;
    }
}
