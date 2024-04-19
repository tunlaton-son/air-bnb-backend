package com.core.airbnbclonebackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Table(name = "LISTING_ADDRESS")
@Entity(name = "LISTING_ADDRESS")
@NoArgsConstructor
@Data
public class ListingAddress {

    @Id
    @Column(name = "ID", length = 50)
    private UUID id;

    @OneToOne(mappedBy = "listingAddress")
    @JsonBackReference
    private Listing listing;

    @Column(name = "LATITUDE", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "STREET")
    private String street;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "CREATED_BY",length = 50)
    private String createdBy;

    @Column(name = "CREATE_TS")
    private Date createTs = new Date();

    @Column(name = "UPDATED_BY",length = 50)
    private String updatedBy;

    @Column(name = "UPDATE_TS")
    private Date updateTs = new Date();

    public ListingAddress(UUID id, Listing listing, BigDecimal latitude, BigDecimal longitude, String houseNumber, String street, String city, String state, String postalCode, String country) {
        this.id = id;
        this.listing = listing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    @Override
    public String toString() {
        return "";
    }
}
