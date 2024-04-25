package com.core.airbnbclonebackend.dto.request.verification;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName("emailverification")
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequest {

    private String userId;

    private String token;

    private String otp;
}
