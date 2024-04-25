package com.core.airbnbclonebackend.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.UUID;

public interface HostingListingResult {

    @Value("#{target.listingId}")
    UUID getListingId();

    @Value("#{target.title}")
    String getTitle();

    @Value("#{target.status}")
    String getStatus();

    @Value("#{target.coverImage}")
    String getCoverImage();

    @Value("#{target.address}")
    String getAddress();

    @Value("#{target.create_ts}")
    Date getCreateTs();
}
