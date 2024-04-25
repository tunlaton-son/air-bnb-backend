package com.core.airbnbclonebackend.repository;

import com.core.airbnbclonebackend.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String>  {
}
