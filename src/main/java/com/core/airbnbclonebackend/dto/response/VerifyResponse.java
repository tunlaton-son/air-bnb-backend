package com.core.airbnbclonebackend.dto.response;

import com.core.airbnbclonebackend.entity.GovernmentDoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResponse {

    private UUID listingId;

    private String listingTitle;

    private String listingAddress;

    private boolean identityVerified;

    private boolean accountVerified;

    private boolean phoneVerified;

    private String coverImageSrc;

    private String frontImageSrc;

    private String backImageSrc;

    private String idType;

}
