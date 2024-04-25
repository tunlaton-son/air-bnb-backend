package com.core.airbnbclonebackend.controllers.v1;

import com.core.airbnbclonebackend.dto.request.governmentdoc.GovernmentDocRequest;
import com.core.airbnbclonebackend.dto.request.verification.EmailVerificationRequest;
import com.core.airbnbclonebackend.dto.request.verification.VerifyPhoneRequest;
import com.core.airbnbclonebackend.dto.response.EmailTokenResponse;
import com.core.airbnbclonebackend.dto.response.VerifyResponse;
import com.core.airbnbclonebackend.entity.GovernmentDoc;
import com.core.airbnbclonebackend.service.VerificationService;
import com.core.airbnbclonebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/verify")
@AllArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    private final UserService userService;

    @GetMapping(path = "")
    public ResponseEntity<VerifyResponse> getVerifyDataByUserIdAndListingId(@RequestParam String userId, @RequestParam String listingId) {
        VerifyResponse verifyResponse = userService.getVerifyDataByUserIdAndListingId(userId, listingId);

        return ResponseEntity.status(200).body(verifyResponse);
    }

    @PostMapping(path = "/identity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GovernmentDoc> saveVerifyIdentity(@Valid @RequestBody GovernmentDocRequest governmentDocRequest) {

        GovernmentDoc governmentDoc = verificationService.saveVerifyIdentity(governmentDocRequest);

        return ResponseEntity.status(200).body(governmentDoc);
    }

    @PostMapping(path = "/identity/upload/images", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GovernmentDoc> uploadVerifyIdentityImages(
            @RequestPart(name = "userId") String userId
            , @RequestPart(name = "imagesPath") String imagesPath
            , @RequestParam(name = "images") List<MultipartFile> images
    ) {
        GovernmentDoc governmentDoc = verificationService.uploadImages(userId, imagesPath, images);
        return ResponseEntity.status(200).body(governmentDoc);
    }

    @PostMapping(path = "/phone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVerifyPhone(@Valid @RequestBody VerifyPhoneRequest verifyPhoneRequest) {

        verificationService.saveVerifyPhone(verifyPhoneRequest);
        return ResponseEntity.status(200).body("success");
    }

    @PostMapping(path = "/email/generate-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailTokenResponse> generateOtp(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {

        EmailTokenResponse emailTokenResponse = verificationService.generateOtp(emailVerificationRequest);

        return ResponseEntity.status(200).body(emailTokenResponse);
    }

    @PostMapping(path = "/email/otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveVerifyEmail(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {

        return verificationService.saveVerifyEmail(emailVerificationRequest);
    }

}
