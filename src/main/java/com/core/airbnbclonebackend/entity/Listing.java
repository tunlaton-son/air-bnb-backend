package com.core.airbnbclonebackend.entity;

import com.core.airbnbclonebackend.enums.Amenities;
import com.core.airbnbclonebackend.enums.ConfirmReservationType;
import com.core.airbnbclonebackend.enums.GuestType;
import com.core.airbnbclonebackend.enums.Step;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Table(name = "LISTING")
@Entity(name = "LISTING")
@NoArgsConstructor
@Data
public class Listing implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 50)
    private UUID id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "STEP")
    private Step step;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "PLACE_TYPE")
    private String placeType;

    @Column(name = "GUEST_COUNT")
    private Integer guestCount;

    @Column(name = "ROOM_COUNT")
    private Integer roomCount;

    @Column(name = "BATH_ROOM_COUNT")
    private Integer bathroomCount;

    @Column(name = "IS_BED_ROOM_HAVE_LOCK")
    private Boolean isBedRoomHaveLock;

    @Column(name = "AMENITIES")
    private List<Amenities> amenities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "listing")
    @JsonManagedReference
    private List<ListingImages> listingImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "listing")
    @JsonManagedReference
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @JsonManagedReference
    private ListingAddress listingAddress;

    @Column(name = "CONFIRM_RESERVATION_TYPE")
    private ConfirmReservationType confirmReservationType;

    @Column(name = "GUEST_TYPE")
    private GuestType guestType;

    @Column(name = "IS_NEW_LISTING_PROMOTION")
    private Boolean isNewListingPromotion = true;

    @Column(name = "IS_WEEKLY_DISCOUNT")
    private Boolean isWeeklyDiscount = true;

    @Column(name = "WEEKLY_DISCOUNT")
    private BigDecimal weeklyDiscount = new BigDecimal(10);

    @Column(name = "IS_MONTHLY_DISCOUNT")
    private Boolean isMonthlyDiscount = true;

    @Column(name = "MONTHLY_DISCOUNT")
    private BigDecimal monthlyDiscount = new BigDecimal(20);

    @Column(name = "HAS_SECURITY_CAMERAS")
    private Boolean hasSecurityCameras = false;

    @Column(name = "HAS_NOISE_MONITORS")
    private Boolean hasNoiseMonitors = false;

    @Column(name = "HAS_WEAPONS")
    private Boolean hasWeapons = false;

    @Column(name = "IS_PUBLISHED")
    private Boolean isPublished = false;

    @Column(name = "CREATED_BY",length = 50)
    private String createdBy;

    @Column(name = "CREATE_TS")
    private Date createTs = new Date();

    @Column(name = "UPDATED_BY",length = 50)
    private String updatedBy;

    @Column(name = "UPDATE_TS")
    private Date updateTs = new Date();

    public Listing(UUID id ,String title, BigDecimal price, String description, String category, String placeType, Integer guestCount, Integer roomCount, Integer bathroomCount, Boolean isBedRoomHaveLock, List<Amenities> amenities, ConfirmReservationType confirmReservationType, GuestType guestType, Boolean isNewListingPromotion, Boolean isWeeklyDiscount, BigDecimal weeklyDiscount, Boolean isMonthlyDiscount, BigDecimal monthlyDiscount, Boolean hasSecurityCameras, Boolean hasNoiseMonitors, Boolean hasWeapons, Step step, ListingAddress listingAddress, User user) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.placeType = placeType;
        this.guestCount = guestCount;
        this.roomCount = roomCount;
        this.bathroomCount = bathroomCount;
        this.isBedRoomHaveLock = isBedRoomHaveLock;
        this.amenities = amenities;
        this.confirmReservationType = confirmReservationType;
        this.guestType = guestType;
        this.isNewListingPromotion = isNewListingPromotion;
        this.isWeeklyDiscount = isWeeklyDiscount;
        this.weeklyDiscount = weeklyDiscount;
        this.isMonthlyDiscount = isMonthlyDiscount;
        this.monthlyDiscount = monthlyDiscount;
        this.hasSecurityCameras = hasSecurityCameras;
        this.hasNoiseMonitors = hasNoiseMonitors;
        this.hasWeapons = hasWeapons;
        this.step = step;
        this.listingAddress = listingAddress;
        this.user = user;
    }
}
