package com.core.airbnbclonebackend.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.core.airbnbclonebackend.dto.request.governmentdoc.GovernmentDocRequest;
import com.core.airbnbclonebackend.dto.request.verification.EmailVerificationRequest;
import com.core.airbnbclonebackend.dto.request.verification.VerifyPhoneRequest;
import com.core.airbnbclonebackend.dto.response.EmailTokenResponse;
import com.core.airbnbclonebackend.entity.*;
import com.core.airbnbclonebackend.enums.IdType;
import com.core.airbnbclonebackend.repository.EmailVerificationRepository;
import com.core.airbnbclonebackend.repository.GovernmentDocRepository;
import com.core.airbnbclonebackend.repository.ListingRepository;
import com.core.airbnbclonebackend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Validated
public class VerificationService {

    @Value("${amazonS3.url}")
    private String s3Url;

    private Random random = new Random();

    private final GovernmentDocRepository governmentDocRepository;

    private final UserRepository userRepository;

    private final FileStorageService fileStorageService;

    private final ListingRepository listingRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final MailSender mailSender;

    private final AmazonSimpleEmailService simpleEmailService;

    public VerificationService(GovernmentDocRepository governmentDocRepository, UserRepository userRepository, FileStorageService fileStorageService, ListingRepository listingRepository, EmailVerificationRepository emailVerificationRepository, MailSender mailSender, AmazonSimpleEmailService simpleEmailService) {
        this.governmentDocRepository = governmentDocRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.listingRepository = listingRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.mailSender = mailSender;
        this.simpleEmailService = simpleEmailService;
    }

    public GovernmentDoc saveVerifyIdentity(GovernmentDocRequest governmentDocRequest) {

        User user = userRepository.findById(UUID.fromString(governmentDocRequest.getUserId())).orElse(null);

        if (user == null) {
            return null;
        }

        UUID id = UUID.randomUUID();
        if (user.getGovernmentDoc() != null) {
            id = user.getGovernmentDoc().getId();
        }

        GovernmentDoc governmentDoc = new GovernmentDoc(
                id,
                IdType.valueOf(governmentDocRequest.getIdType()),
                governmentDocRequest.getCountry(),
                user
        );

        user.setGovernmentDoc(governmentDoc);
        userRepository.save(user);

        return governmentDoc;
    }

    public GovernmentDoc uploadImages(String userId, String imagesPath, List<MultipartFile> images) {
        User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

        if (user == null) {
            return null;
        }

        GovernmentDoc governmentDoc = null;
        List<FileDescriptor> fileDescriptorList;
        if (user.getGovernmentDoc() != null) {
            governmentDoc = new GovernmentDoc(
                    user.getGovernmentDoc().getId(),
                    user.getGovernmentDoc().getIdType(),
                    user.getGovernmentDoc().getCountry(),
                    user
            );

            ResponseEntity<?> response = fileStorageService.uploadFileToS3(imagesPath, images);
            fileDescriptorList = (List<FileDescriptor>) response.getBody();

            int count = 1;
            assert fileDescriptorList != null;
            for (FileDescriptor image : fileDescriptorList) {
                String imageSrc = s3Url + "/verify_images/" + image.getId().toString() + "." + image.getExtension();
                if (count == 1) {
                    governmentDoc.setFrontImageSrc(imageSrc);
                } else {
                    governmentDoc.setBackImageSrc(governmentDoc.getIdType().equals(IdType.PASSPORT) ? null : imageSrc);
                }
                count++;
            }

            user.setGovernmentDoc(governmentDoc);
            user.setIsIdentityVerified(true);

            userRepository.save(user);
        }

        return governmentDoc;
    }

    public void saveVerifyPhone(VerifyPhoneRequest verifyPhoneRequest) {
        User user = userRepository.findById(UUID.fromString(verifyPhoneRequest.getUserId())).orElse(null);

        if (user != null) {

            user.setPhone(verifyPhoneRequest.getPhone());
            user.setIsPhoneVerified(true);

            userRepository.save(user);
        }
    }

    public EmailTokenResponse generateOtp(EmailVerificationRequest emailVerificationRequest){
        User user = userRepository.findById(UUID.fromString(emailVerificationRequest.getUserId())).orElse(null);
        if (user == null) {
            return null;
        }

        int otpExpiryTime = 5;
        String sessionToken = getRandomString(32, false);
        String otp = getRandomString(6, true);
        Double expiryAt = Math.floor(new Date().getTime() / 1000.00) + otpExpiryTime * 60;

        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<>();
        String[] emails = new String[]{user.getEmail()};
        Collections.addAll(toAddresses, Objects.requireNonNull(emails));
        destination.setToAddresses(toAddresses);

        SendEmailRequest emailRequest = getSendEmailRequest(otp, destination);

        simpleEmailService.sendEmail(emailRequest);

        EmailVerification emailVerification = new EmailVerification(
                sessionToken + "_" + otp,
                user.getEmail(),
                expiryAt
        );

        emailVerificationRepository.save(emailVerification);

        return new EmailTokenResponse(
                sessionToken
        );
    }

    public ResponseEntity<String> saveVerifyEmail(EmailVerificationRequest emailVerificationRequest){
        User user = userRepository.findById(UUID.fromString(emailVerificationRequest.getUserId())).orElse(null);
        if (user != null) {

            EmailVerification emailVerification = emailVerificationRepository.findById(emailVerificationRequest.getToken() + "_" + emailVerificationRequest.getOtp()).orElse(null);

            if (emailVerification != null && (emailVerification.getExpiryAt() > Math.floor(new Date().getTime() / 1000.00)) ) {
                user.setEmail(emailVerification.getEmail());
                user.setIsAccountVerified(true);

                userRepository.save(user);
                return ResponseEntity.status(200).body("Validated");
            }
        }

        return ResponseEntity.status(422).body("Cannot validate OTP.");
    }

    private static @NotNull SendEmailRequest getSendEmailRequest(String otp, Destination destination) {
        Content content = new Content();
        content.setData(otp);

        Body body = new Body();
        body.setText(content);

        Message message = new Message();
        message.setBody(body);

        Content subject = new Content();
        subject.setData("Use this code to verify your email at AIR BNB");
        message.setSubject(subject);

        SendEmailRequest emailRequest = new SendEmailRequest();
        emailRequest.setDestination(destination);
        emailRequest.setMessage(message);
        emailRequest.setSource("tunlaton@hotmail.com");
        return emailRequest;
    }

    private String getRandomString(int length, boolean  onlyNumbers) {
        StringBuilder result = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        if(onlyNumbers) {
            characters = "0123456789";
        }

        int charactersLength = characters.length();
        for(int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(charactersLength)));
        }

        return result.toString();
    }
}
