package com.core.airbnbclonebackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table(name = "LISTING_IMAGES")
@Entity(name = "LISTING_IMAGES")
@NoArgsConstructor
@Data
public class ListingImages implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 50)
    private UUID id;

    @Column(name = "IMAGE_SRC")
    private String imageSrc;

    @Column(name = "SEQ")
    private Integer seq = 1;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "listingId")
    @JsonBackReference
    private Listing listing;

    @Column(name = "IS_COVER_IMAGE")
    private Boolean isCoverImage = false;

    @Column(name = "CREATED_BY",length = 50)
    private String createdBy;

    @Column(name = "CREATE_TS")
    private Date createTs = new Date();

    @Column(name = "UPDATED_BY",length = 50)
    private String updatedBy;

    @Column(name = "UPDATE_TS")
    private Date updateTs = new Date();

    public ListingImages(UUID id, String imageSrc, Integer seq, Listing listing, Boolean isCoverImage) {
        this.id = id;
        this.imageSrc = imageSrc;
        this.seq = seq;
        this.listing = listing;
        this.isCoverImage = isCoverImage;
    }

    @Override
    public String toString() {
        return "";
    }
}
