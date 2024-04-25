package com.core.airbnbclonebackend.dto.request.verification;

import com.core.airbnbclonebackend.dto.request.user.DuplicatedPhoneConstraint;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName("phoneverify")
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPhoneRequest {

    @NotBlank(message = "User Id is required field")
    private String userId;

    @NotBlank(message = "Phone is required field")
    @Size(min = 10, message = "Phone number must be 10 digit")
    @DuplicatedPhoneConstraint
    private String phone;
}
