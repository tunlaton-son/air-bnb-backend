package com.core.airbnbclonebackend.mapper;

import com.core.airbnbclonebackend.dto.response.ListingResponse;
import com.core.airbnbclonebackend.entity.Listing;
import com.core.airbnbclonebackend.enums.Step;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ListingResponseMapper {

    @Mapping(source = "id", target = "listingId")
    @Mapping(source = "isBedRoomHaveLock", target = "haveLock")
    @Mapping(source = "isNewListingPromotion", target = "hasNewListingPromotion")
    @Mapping(source = "isWeeklyDiscount", target = "hasWeeklyDiscount")
    @Mapping(source = "isMonthlyDiscount", target = "hasMonthlyDiscount")
    @Mapping(source = "step", target = "step", qualifiedByName = "mapStepToInteger")
    ListingResponse mapperListingResponse(Listing listing);

    @Named("mapStepToInteger")
    default Integer mapStepToInteger(Step step) {
        return step.getValue();
    }
}
