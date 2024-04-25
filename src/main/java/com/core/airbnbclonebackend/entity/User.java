package com.core.airbnbclonebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.*;

@Table(name = "_USER")
@Entity(name = "User")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 50)
    private UUID id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "government_doc_id", referencedColumnName = "id")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private GovernmentDoc governmentDoc;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Listing> listingList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Reservation> reservationList = new ArrayList<>();

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "IS_IDENTITY_VERIFIED")
    private Boolean isIdentityVerified = false;

    @Column(name = "IS_PHONE_VERIFIED")
    private Boolean isPhoneVerified = false;

    @Column(name = "IS_ACCOUNT_VERIFIED")
    private Boolean isAccountVerified = false;

    public User(String email, String name, String phone, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
