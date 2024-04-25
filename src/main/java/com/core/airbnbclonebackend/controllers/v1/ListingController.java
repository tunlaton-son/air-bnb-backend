package com.core.airbnbclonebackend.controllers.v1;

import com.core.airbnbclonebackend.dto.request.listing.ListingRequest;
import com.core.airbnbclonebackend.dto.response.ListingResponse;
import com.core.airbnbclonebackend.dto.response.hosting.HostingListingResponse;
import com.core.airbnbclonebackend.entity.ListingImages;
import com.core.airbnbclonebackend.repository.ListingImagesRepository;
import com.core.airbnbclonebackend.service.ListingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/listings")
@AllArgsConstructor
public class ListingController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private ListingImagesRepository listingImagesRepository;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListingResponse> getListingById(@RequestParam String listingId) {
        return listingService.getListingById(listingId);
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ListingResponse> saveListing(@Valid @RequestBody ListingRequest listingRequest) {
        return listingService.saveListing(listingRequest);
    }

    @PostMapping(path = "/upload/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ListingImages>> uploadImages(@RequestPart(name   = "listingId") String listingId
                                      , @RequestPart(name  = "imagesPath") String imagesPath
                                      , @RequestParam(name = "images") List<MultipartFile> images
                                      , @RequestParam(name = "listingImages", required = false) String listingImagesReq) throws JsonProcessingException {

        List<ListingImages> listingImages = listingService.uploadImages(listingId, imagesPath,images, listingImagesReq);

        return ResponseEntity.status(200)
                .body(listingImages);
    }

    @DeleteMapping(path = "/remove/images/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeImage(@PathVariable("id") String id) {

        listingImagesRepository.deleteById(UUID.fromString(id));
        return ResponseEntity.status(200)
                .body("OK");
    }

    @PostMapping(path = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publishListing(@Valid @RequestBody ListingRequest listingRequest){

        return listingService.publishListing(listingRequest);
    }

    @GetMapping(path = "/hasListing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> checkUserHasListing(@RequestParam String userId){
        return listingService.checkUserHasListing(userId);
    }

    @GetMapping(path = "/hosting/listings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HostingListingResponse>> getHostingListingsByUserId(@RequestParam String userId){

        return listingService.getHostingListingsByUserId(userId);
    }

    @GetMapping(path = "/hosting/listings/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HostingListingResponse>> searchHostingListingsByKeyword(@RequestParam String userId, @RequestParam String keyWord){
        return listingService.searchHostingListingsByKeyword(UUID.fromString(userId) ,keyWord);
    }

    @DeleteMapping(path =  "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeListingById(@RequestParam String listingId){
        return listingService.removeListingById(listingId);
    }
}
