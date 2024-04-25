package com.core.airbnbclonebackend.dto.request.governmentdoc;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName("governmentDoc")
@AllArgsConstructor
@NoArgsConstructor
public class GovernmentDocRequest {

    private String userId;

    private String idType;

    private String country;
}
