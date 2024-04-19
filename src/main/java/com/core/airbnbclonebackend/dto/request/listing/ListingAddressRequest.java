package com.core.airbnbclonebackend.dto.request.listing;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@JsonRootName("listingAddress")
@AllArgsConstructor
@NoArgsConstructor
public class ListingAddressRequest {

    private String id;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String houseNumber;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String country;
}
