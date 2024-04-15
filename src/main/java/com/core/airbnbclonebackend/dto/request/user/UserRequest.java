package com.core.airbnbclonebackend.dto.request.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@JsonRootName("user")
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Email is required field")
    @Email(message = "Invalid Email Format")
    @DuplicatedEmailConstraint
    private String email;

    @NotBlank(message = "Name is required field")
    @Size(max = 255, message = "Name must be less than or equal to 255 characters")
    private String name;

    @Size(max = 10, message = "Phone number must be 10 digit")
    @Size(min = 10, message = "Phone number must be 10 digit")
    @DuplicatedPhoneConstraint
    private String phone;

    @Size(max = 255, message = "Password must be less than or equal to 255 characters")
    @Size(min = 8, message = "Password must be more than or equal to 8 characters")
    private String password;

}
