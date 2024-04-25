package com.core.airbnbclonebackend.service;

import com.core.airbnbclonebackend.dto.request.user.UserRequest;
import com.core.airbnbclonebackend.dto.response.VerifyResponse;
import com.core.airbnbclonebackend.entity.*;
import com.core.airbnbclonebackend.repository.ListingRepository;
import com.core.airbnbclonebackend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ListingRepository listingRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.listingRepository = listingRepository;
    }

    public User createUser(@Valid UserRequest userRequest) {
        User user =
                new User(
                        userRequest.getEmail(),
                        userRequest.getName(),
                        userRequest.getPhone(),
                        passwordEncoder.encode(userRequest.getPassword())
                );
        userRepository.save(user);
        return user;
    }

    public VerifyResponse getVerifyDataByUserIdAndListingId(String userId, String listingId) {
        User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        Listing listing = listingRepository.findListingByIdAndUser(UUID.fromString(listingId), user).orElse(null);

        if (user == null || listing == null) {
            return null;
        }

        ListingAddress address = listing.getListingAddress();
        String addressStr = address.getHouseNumber() + " " + address.getStreet() + " " + address.getCity() + " " + address.getPostalCode() + " ," + address.getCountry();
        String coverImage = listing.getListingImages().stream().filter(ListingImages::getIsCoverImage).map(ListingImages::getImageSrc).findFirst().orElse(null);

        GovernmentDoc governmentDoc = user.getGovernmentDoc();

        String frontImageSrc = null;
        String backImageSrc = null;
        String idType = null;
        if (governmentDoc != null) {
            frontImageSrc = governmentDoc.getFrontImageSrc();
            backImageSrc = governmentDoc.getBackImageSrc();
            idType = String.valueOf(governmentDoc.getIdType());
        }
        return new VerifyResponse(
                listing.getId(),
                listing.getTitle(),
                addressStr,
                user.getIsIdentityVerified(),
                user.getIsAccountVerified(),
                user.getIsPhoneVerified(),
                coverImage,
                frontImageSrc,
                backImageSrc,
                idType
        );
    }
}
