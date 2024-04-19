package com.core.airbnbclonebackend.controllers.v1;

import com.core.airbnbclonebackend.dto.request.listing.ListingImagesRequest;
import com.core.airbnbclonebackend.dto.request.listing.ListingRequest;
import com.core.airbnbclonebackend.dto.request.user.UserRequest;
import com.core.airbnbclonebackend.dto.response.UserResponse;
import com.core.airbnbclonebackend.dto.response.UserWithToken;
import com.core.airbnbclonebackend.entity.Listing;
import com.core.airbnbclonebackend.entity.ListingImages;
import com.core.airbnbclonebackend.entity.User;
import com.core.airbnbclonebackend.repository.ListingImagesRepository;
import com.core.airbnbclonebackend.repository.ListingRepository;
import com.core.airbnbclonebackend.service.ListingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveListing(@RequestPart(name = "listingRequest") String listingRequest) throws JsonProcessingException {


        Listing listing = listingService.saveListing(listingRequest);

        return ResponseEntity.status(200)
                .body(listing);
    }

    @PostMapping(path = "/upload/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadImages(@RequestPart(name   = "listingId") String listingId
                                      , @RequestPart(name  = "imagesPath") String imagesPath
                                      , @RequestParam(name = "images") List<MultipartFile> images
                                      , @RequestParam(name = "listingImages", required = false) String listingImagesReq) throws JsonProcessingException {

        List<ListingImages> listingImages = listingService.uploadImages(listingId, imagesPath,images, listingImagesReq);

        return ResponseEntity.status(200)
                .body(listingImages);
    }

    @DeleteMapping(path = "/remove/images/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeImage(@PathVariable("id") String id) {

        listingImagesRepository.deleteById(UUID.fromString(id));
        return ResponseEntity.status(200)
                .body("OK");
    }
}
