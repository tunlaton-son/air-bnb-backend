package com.core.airbnbclonebackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "EMAIL_VERIFICATION")
@Entity(name = "EMAIL_VERIFICATION")
@NoArgsConstructor
@Data
public class EmailVerification {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EXPIRY_AT")
    private Double expiryAt;

    public EmailVerification(String id, String email, Double expiryAt) {
        this.id = id;
        this.email = email;
        this.expiryAt = expiryAt;
    }
}
