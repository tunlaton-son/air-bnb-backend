package com.core.airbnbclonebackend.dto.response.hosting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostingListingResponse {

    private UUID listingId;

    private String title;

    private String status;

    private String coverImageSrc;

    private String address;

    private String startListingDate;
}
