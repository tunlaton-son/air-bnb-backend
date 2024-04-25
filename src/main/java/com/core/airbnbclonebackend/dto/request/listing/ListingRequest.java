package com.core.airbnbclonebackend.dto.request.listing;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonRootName("listing")
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequest {

    private String listingId;

    private String userId;

    private String title;

    private BigDecimal price;

    private Integer step;

    private String description;

    private String category;

    private String placeType;

    private ListingAddressRequest listingAddress;

    private Integer guestCount;

    private Integer roomCount;

    private Integer bathroomCount;

    private Boolean isBedRoomHaveLock;

    private List<String> amenities = new ArrayList<>();

    private String confirmReservationType;

    private String guestType;

    private Boolean isNewListingPromotion;

    private Boolean isWeeklyDiscount;

    private BigDecimal weeklyDiscount;

    private Boolean isMonthlyDiscount;

    private BigDecimal monthlyDiscount;

    private Boolean hasSecurityCameras;

    private Boolean hasNoiseMonitors;

    private Boolean hasWeapons;
}
