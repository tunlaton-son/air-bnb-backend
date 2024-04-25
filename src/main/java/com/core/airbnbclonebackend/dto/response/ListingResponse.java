package com.core.airbnbclonebackend.dto.response;

import com.core.airbnbclonebackend.entity.ListingAddress;
import com.core.airbnbclonebackend.enums.Step;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListingResponse {

    private UUID listingId;

    private String category;

    private String placeType;

    private ListingAddressResponse listingAddress;

    private List<ListingImageResponse> listingImages;

    private int guestCount;

    private int roomCount;

    private int bathroomCount;

    private boolean haveLock;

    private List<String> amenities;

    private String title;

    private String description;

    private String guestType;

    private BigDecimal price;

    private boolean hasNewListingPromotion;

    private boolean hasWeeklyDiscount;

    private BigDecimal weeklyDiscount;

    private boolean hasMonthlyDiscount;

    private BigDecimal monthlyDiscount;

    private boolean hasSecurityCameras;

    private boolean hasNoiseMonitors;

    private boolean hasWeapons;

    private Integer step;
}
