package com.core.airbnbclonebackend.service;

import com.core.airbnbclonebackend.dto.request.listing.ListingImagesRequest;
import com.core.airbnbclonebackend.dto.request.listing.ListingRequest;
import com.core.airbnbclonebackend.entity.FileDescriptor;
import com.core.airbnbclonebackend.entity.Listing;
import com.core.airbnbclonebackend.entity.ListingAddress;
import com.core.airbnbclonebackend.entity.ListingImages;
import com.core.airbnbclonebackend.enums.Amenities;
import com.core.airbnbclonebackend.enums.ConfirmReservationType;
import com.core.airbnbclonebackend.enums.GuestType;
import com.core.airbnbclonebackend.enums.Step;
import com.core.airbnbclonebackend.repository.ListingImagesRepository;
import com.core.airbnbclonebackend.repository.ListingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Validated
public class ListingService {

    private static Logger logger = LoggerFactory.getLogger(ListingService.class);

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ListingImagesRepository listingImagesRepository;

    @Value("${amazonS3.url}")
    private String s3Url;

    public Listing saveListing(String listingRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ListingRequest data = objectMapper.readValue(listingRequest, ListingRequest.class);

        UUID listingId = data.getListingId() != null ? UUID.fromString(data.getListingId()) : null;
        Optional<Listing> queryListing = listingId != null ? listingRepository.findById(listingId) : Optional.empty();

        Step queryStep   = queryListing.map(Listing::getStep).orElse(null);
        Step reqStep     = data.getStep() != null ? Step.values()[data.getStep()] : queryStep;
        Step listingStep = queryStep == null ? Step.STEP1 : queryStep.compareTo(reqStep) >= 0 ? queryStep : reqStep;

        List<Amenities> amenities = new ArrayList<>();
        for (String amenity : data.getAmenities()) {
            try{
                amenities.add(Amenities.valueOf(amenity));
            }catch (Exception ignored){

            }
        }

        ListingAddress listingAddress = null;
        if(queryListing.isPresent()) {
            Listing listing = queryListing.get();
            listingAddress = new ListingAddress(
                    listing.getListingAddress().getId(),
                    listing,
                    data.getListingAddress().getLatitude(),
                    data.getListingAddress().getLongitude(),
                    data.getListingAddress().getHouseNumber(),
                    data.getListingAddress().getStreet(),
                    data.getListingAddress().getCity(),
                    data.getListingAddress().getState(),
                    data.getListingAddress().getPostalCode(),
                    data.getListingAddress().getCountry()
            );
        }else{
            listingAddress = new ListingAddress(
                    UUID.randomUUID(),
                    null,
                    data.getListingAddress().getLatitude(),
                    data.getListingAddress().getLongitude(),
                    data.getListingAddress().getHouseNumber(),
                    data.getListingAddress().getStreet(),
                    data.getListingAddress().getCity(),
                    data.getListingAddress().getState(),
                    data.getListingAddress().getPostalCode(),
                    data.getListingAddress().getCountry()
            );
        }

        Listing listing = new Listing(
                listingId != null ? listingId : UUID.randomUUID(),
                data.getTitle(),
                data.getPrice(),
                data.getDescription(),
                data.getCategory(),
                data.getPlaceType(),
                data.getGuestCount(),
                data.getRoomCount(),
                data.getBathroomCount(),
                data.getIsBedRoomHaveLock(),
                amenities,
                data.getConfirmReservationType() != null ? ConfirmReservationType.valueOf(data.getConfirmReservationType()) : null,
                data.getGuestType() != null ? GuestType.valueOf(data.getGuestType()) : null,
                data.getIsNewListingPromotion(),
                data.getIsWeeklyDiscount(),
                data.getWeeklyDiscount(),
                data.getIsMonthlyDiscount(),
                data.getMonthlyDiscount(),
                data.getHasSecurityCameras(),
                data.getHasNoiseMonitors(),
                data.getHasWeapons(),
                listingStep,
                listingAddress
        );

        listingRepository.save(listing);
        return listing;
    }


    public List<ListingImages> uploadImages(String listingId, String imagesPath, List<MultipartFile> images, String listingImagesReq) throws JsonProcessingException {

        UUID id = listingId != null ? UUID.fromString(listingId) : null;
        Optional<Listing> queryListing = listingId != null ? listingRepository.findById(id) : Optional.empty();
        List<FileDescriptor> fileDescriptorList = new ArrayList<>();
        List<ListingImages> listingImagesList = new ArrayList<>();

        if(queryListing.isPresent() && !images.isEmpty()){

            ResponseEntity<?> response = fileStorageService.uploadFileToS3(imagesPath, images);
            fileDescriptorList = (List<FileDescriptor>) response.getBody();

            assert fileDescriptorList != null;
            Listing listing = queryListing.get();
            listingImagesList = createListingImages(fileDescriptorList, listing);

            ObjectMapper objectMapper = new ObjectMapper();
            List<ListingImagesRequest> listingImagesRequestList = objectMapper.readValue(listingImagesReq, new TypeReference<List<ListingImagesRequest>>(){});

            //delete old images
            queryListing.ifPresent(value -> deleteImage(listingImagesRequestList));

            listingImagesRepository.saveAll(listingImagesList);
            listingRepository.save(listing);
        }

        return listingImagesList;
    }

    private List<ListingImages> createListingImages(List<FileDescriptor> images, Listing listing) {
        List<ListingImages> listingImagesList = new ArrayList<>();
        Integer count = 1;

        for(FileDescriptor image : images){
            String imageSrc = s3Url + "/listing_images/" + image.getId().toString() + "." + image.getExtension();
            if(count == 1) {
                ListingImages listingImages = new ListingImages(
                        UUID.randomUUID(),
                        imageSrc,
                        count,
                        listing,
                        true
                );
                listingImagesList.add(listingImages);
            }else{
                ListingImages listingImages = new ListingImages(
                        UUID.randomUUID(),
                        imageSrc,
                        count,
                        listing,
                        false
                );
                listingImagesList.add(listingImages);
            }

            count++;
        }

        return listingImagesList;
    }

    private void deleteImage(List<ListingImagesRequest> listingImagesRequests){
        try {

            List<UUID> uuidList = listingImagesRequests.stream().filter(e->e.getId() != null).map(e->UUID.fromString(e.getId())).toList();
            listingImagesRepository.deleteAllByIdInBatch(uuidList);

        }catch (Exception ex){
            logger.error(ex.toString());
            throw  ex;
        }
    }
}
