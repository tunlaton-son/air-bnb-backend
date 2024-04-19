package com.core.airbnbclonebackend.repository;

import com.core.airbnbclonebackend.entity.Listing;
import com.core.airbnbclonebackend.entity.ListingImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListingImagesRepository extends JpaRepository<ListingImages, UUID> {
}
