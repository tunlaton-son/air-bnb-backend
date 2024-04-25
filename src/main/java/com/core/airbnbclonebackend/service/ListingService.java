package com.core.airbnbclonebackend.service;

import com.core.airbnbclonebackend.dto.request.listing.ListingImagesRequest;
import com.core.airbnbclonebackend.dto.request.listing.ListingRequest;
import com.core.airbnbclonebackend.dto.response.ListingResponse;
import com.core.airbnbclonebackend.dto.response.hosting.HostingListingResponse;
import com.core.airbnbclonebackend.entity.*;
import com.core.airbnbclonebackend.enums.Amenities;
import com.core.airbnbclonebackend.enums.ConfirmReservationType;
import com.core.airbnbclonebackend.enums.GuestType;
import com.core.airbnbclonebackend.enums.Step;
import com.core.airbnbclonebackend.mapper.ListingResponseMapper;
import com.core.airbnbclonebackend.projection.HostingListingResult;
import com.core.airbnbclonebackend.repository.ListingImagesRepository;
import com.core.airbnbclonebackend.repository.ListingRepository;
import com.core.airbnbclonebackend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Validated
public class ListingService {

    private static final Logger logger = LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;

    private final FileStorageService fileStorageService;

    private final ListingImagesRepository listingImagesRepository;
    private final UserRepository userRepository;

    private final ListingResponseMapper listingResponseMapper;

    public ListingService(ListingRepository listingRepository, FileStorageService fileStorageService, ListingImagesRepository listingImagesRepository, UserRepository userRepository, ListingResponseMapper listingResponseMapper) {
        this.listingRepository = listingRepository;
        this.fileStorageService = fileStorageService;
        this.listingImagesRepository = listingImagesRepository;
        this.userRepository = userRepository;
        this.listingResponseMapper = listingResponseMapper;
    }

    @Value("${amazonS3.url}")
    private String s3Url;

    public ResponseEntity<ListingResponse> getListingById(String id) {
        try{
            if(id == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Listing listing = listingRepository.findById(UUID.fromString(id)).orElse(null);

            if(listing == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ListingResponse listingResponse = listingResponseMapper.mapperListingResponse(listing);

            return new ResponseEntity<>(listingResponse, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ListingResponse> saveListing(ListingRequest listingRequest) {

        try {
            User user = userRepository.findById(UUID.fromString(listingRequest.getUserId())).orElse(null);
            if (user == null) {
                return new ResponseEntity<>(new ListingResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            UUID listingId = listingRequest.getListingId() != null ? UUID.fromString(listingRequest.getListingId()) : null;
            Optional<Listing> queryListing = listingId != null ? listingRepository.findById(listingId) : Optional.empty();

            Step queryStep = queryListing.map(Listing::getStep).orElse(null);
            Step reqStep = listingRequest.getStep() != null ? Step.values()[listingRequest.getStep()] : queryStep;
            Step listingStep = getListingStep(queryStep, reqStep);

            List<Amenities> amenities = new ArrayList<>();
            for (String amenity : listingRequest.getAmenities()) {
                amenities.add(Amenities.valueOf(amenity));
            }

            ListingAddress listingAddress = null;
            Date createTs = new Date();
            if (queryListing.isPresent()) {
                Listing listing = queryListing.get();
                createTs = listing.getCreateTs();
                listingAddress = new ListingAddress(
                        listing.getListingAddress().getId(),
                        listing,
                        listingRequest.getListingAddress().getLatitude(),
                        listingRequest.getListingAddress().getLongitude(),
                        listingRequest.getListingAddress().getHouseNumber(),
                        listingRequest.getListingAddress().getStreet(),
                        listingRequest.getListingAddress().getCity(),
                        listingRequest.getListingAddress().getState(),
                        listingRequest.getListingAddress().getPostalCode(),
                        listingRequest.getListingAddress().getCountry()
                );
            } else {
                listingAddress = new ListingAddress(
                        UUID.randomUUID(),
                        null,
                        listingRequest.getListingAddress().getLatitude(),
                        listingRequest.getListingAddress().getLongitude(),
                        listingRequest.getListingAddress().getHouseNumber(),
                        listingRequest.getListingAddress().getStreet(),
                        listingRequest.getListingAddress().getCity(),
                        listingRequest.getListingAddress().getState(),
                        listingRequest.getListingAddress().getPostalCode(),
                        listingRequest.getListingAddress().getCountry()
                );
            }

            Listing listing = new Listing(
                    listingId != null ? listingId : UUID.randomUUID(),
                    listingRequest.getTitle(),
                    listingRequest.getPrice(),
                    listingRequest.getDescription(),
                    listingRequest.getCategory(),
                    listingRequest.getPlaceType(),
                    listingRequest.getGuestCount(),
                    listingRequest.getRoomCount(),
                    listingRequest.getBathroomCount(),
                    listingRequest.getIsBedRoomHaveLock(),
                    amenities,
                    listingRequest.getConfirmReservationType() != null ? ConfirmReservationType.valueOf(listingRequest.getConfirmReservationType()) : null,
                    listingRequest.getGuestType() != null ? GuestType.valueOf(listingRequest.getGuestType()) : null,
                    listingRequest.getIsNewListingPromotion(),
                    listingRequest.getIsWeeklyDiscount(),
                    listingRequest.getWeeklyDiscount(),
                    listingRequest.getIsMonthlyDiscount(),
                    listingRequest.getMonthlyDiscount(),
                    listingRequest.getHasSecurityCameras(),
                    listingRequest.getHasNoiseMonitors(),
                    listingRequest.getHasWeapons(),
                    listingStep,
                    listingAddress,
                    user
            );
            listing.setCreateTs(createTs);

            user.getListingList().add(listing);
            userRepository.save(user);

            ListingResponse listingResponse = new ListingResponse();
            listingResponse.setListingId(listing.getId());
            return new ResponseEntity<>(listingResponse, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ListingResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ListingImages> uploadImages(String listingId, String imagesPath, List<MultipartFile> images, String listingImagesReq) throws JsonProcessingException {

        UUID id = listingId != null ? UUID.fromString(listingId) : null;
        Optional<Listing> queryListing = listingId != null ? listingRepository.findById(id) : Optional.empty();
        List<FileDescriptor> fileDescriptorList;
        List<ListingImages> listingImagesList = new ArrayList<>();

        if (queryListing.isPresent() && !images.isEmpty()) {

            ResponseEntity<?> response = fileStorageService.uploadFileToS3(imagesPath, images);
            fileDescriptorList = (List<FileDescriptor>) response.getBody();

            assert fileDescriptorList != null;
            Listing listing = queryListing.get();
            listingImagesList = createListingImages(fileDescriptorList, listing);

            ObjectMapper objectMapper = new ObjectMapper();
            List<ListingImagesRequest> listingImagesRequestList = objectMapper.readValue(listingImagesReq, new TypeReference<List<ListingImagesRequest>>() {
            });

            //delete old images
            queryListing.ifPresent(value -> deleteImage(listingImagesRequestList));

            listingImagesRepository.saveAll(listingImagesList);
        }

        return listingImagesList;
    }

    public ResponseEntity<String> publishListing(ListingRequest listingRequest) {

        try {

            UUID listingId = listingRequest.getListingId() != null ? UUID.fromString(listingRequest.getListingId()) : null;
            Optional<Listing> queryListing = listingId != null ? listingRepository.findById(listingId) : Optional.empty();
            if (queryListing.isPresent()) {
                Listing listing = queryListing.get();
                User user = userRepository.findById(UUID.fromString(listingRequest.getUserId())).orElse(null);

                assert user != null;
                boolean isIdentityVerified = user.getIsIdentityVerified();
                boolean isPhoneVerified = user.getIsPhoneVerified();
                boolean isAccountVerified = user.getIsAccountVerified();

                if (isIdentityVerified && isPhoneVerified && isAccountVerified) {

                    listing.setIsPublished(true);
                    listingRepository.save(listing);

                    return ResponseEntity.status(200)
                            .body("Published Listing successfully");
                }
            }

            return ResponseEntity.status(500)
                    .body("Published Listing unsuccessful");

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(500)
                    .body("Published Listing unsuccessful");
        }
    }

    public ResponseEntity<HashMap<String, Object>> checkUserHasListing(String userId){
        try{

            User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

            assert user != null;
            Boolean hasListing = !user.getListingList().isEmpty();

            return ResponseEntity.ok(
                    new HashMap<String, Object>() {
                        {
                            put("hasListing", hasListing);
                        }
                    });

        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<HostingListingResponse>> getHostingListingsByUserId(String userId){

        try{
            List<HostingListingResult> hostingListingResultList = listingRepository.findListingByUserId(UUID.fromString(userId));
            List<HostingListingResponse> hostingListingResponseList = new ArrayList<>();

            for(HostingListingResult hostingListingResult : hostingListingResultList){
                HostingListingResponse hostingListingResponse = getHostingListingResponse(hostingListingResult);
                hostingListingResponseList.add(hostingListingResponse);
            }

            return new ResponseEntity<>(hostingListingResponseList, HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<HostingListingResponse>> searchHostingListingsByKeyword(UUID uuid, String keyword){
        try{

            List<HostingListingResult> hostingListingResultList = listingRepository.findListingByKeyword(uuid,"%" + keyword.toUpperCase() + "%");
            List<HostingListingResponse> hostingListingResponseList = new ArrayList<>();
            for(HostingListingResult hostingListingResult : hostingListingResultList){
                HostingListingResponse hostingListingResponse = getHostingListingResponse(hostingListingResult);
                hostingListingResponseList.add(hostingListingResponse);
            }

            return new ResponseEntity<>(hostingListingResponseList, HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> removeListingById(String listingId){
        try{

            if(listingId == null){
                return new ResponseEntity<>("Listing Id is missing", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            listingRepository.deleteById(UUID.fromString(listingId));
            return new ResponseEntity<>("Remove listing successful", HttpStatus.OK);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<>("Remove listing unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static @NotNull HostingListingResponse getHostingListingResponse(HostingListingResult hostingListingResult) {
        HostingListingResponse hostingListingResponse = new HostingListingResponse();
        hostingListingResponse.setListingId(hostingListingResult.getListingId());
        hostingListingResponse.setTitle(hostingListingResult.getTitle());
        hostingListingResponse.setStatus(hostingListingResult.getStatus());
        hostingListingResponse.setCoverImageSrc(hostingListingResult.getCoverImage());
        hostingListingResponse.setAddress(hostingListingResult.getAddress());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = sdf.format(hostingListingResult.getCreateTs());
        hostingListingResponse.setStartListingDate(formattedDate);

        return hostingListingResponse;
    }

    private List<ListingImages> createListingImages(List<FileDescriptor> images, Listing listing) {
        List<ListingImages> listingImagesList = new ArrayList<>();
        Integer count = 1;

        boolean isEmpty = listing.getListingImages().isEmpty();
        for (FileDescriptor image : images) {
            String imageSrc = s3Url + "/listing_images/" + image.getId().toString() + "." + image.getExtension();
            if (count == 1 && isEmpty) {
                ListingImages listingImages = new ListingImages(
                        UUID.randomUUID(),
                        imageSrc,
                        count,
                        listing,
                        true
                );
                listingImagesList.add(listingImages);
            } else {
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

    private void deleteImage(List<ListingImagesRequest> listingImagesRequests) {
        try {

            List<UUID> uuidList = listingImagesRequests.stream().filter(e -> e.getId() != null).map(e -> UUID.fromString(e.getId())).toList();
            listingImagesRepository.deleteAllByIdInBatch(uuidList);

        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private Step getListingStep(Step queryStep, Step reqStep) {

        if (queryStep == null) {
            return Step.STEP1;
        }

        return queryStep.compareTo(reqStep) >= 0 ? queryStep : reqStep;
    }
}
