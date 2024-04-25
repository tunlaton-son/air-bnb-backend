package com.core.airbnbclonebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListingAddressResponse {

    private UUID id;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String houseNumber;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String country;
}
