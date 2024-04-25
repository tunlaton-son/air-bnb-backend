package com.core.airbnbclonebackend.dto.request.listing;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@JsonRootName("listingImages")
@AllArgsConstructor
@NoArgsConstructor
public class ListingImagesRequest {

    private String id;

    private String imageSrc;

    private Integer seq;

    private String listingId;

    private MultipartFile file;

    private boolean coverImage;
}
