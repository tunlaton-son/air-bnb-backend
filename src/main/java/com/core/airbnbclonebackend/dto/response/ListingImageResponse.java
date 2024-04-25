package com.core.airbnbclonebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListingImageResponse {

    private String id;

    private String imageSrc;

    private Integer seq;

    private boolean isCoverImage;

    public boolean isCoverImage() {
        return isCoverImage;
    }

    public void setIsCoverImage(boolean isCoverImage) {
        this.isCoverImage = isCoverImage;
    }
}
