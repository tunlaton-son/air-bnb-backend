package com.core.airbnbclonebackend.entity;

import com.core.airbnbclonebackend.enums.IdType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table(name = "GOVERNMENT_DOC")
@Entity(name = "GOVERNMENT_DOC")
@NoArgsConstructor
@Data
public class GovernmentDoc implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 50)
    private UUID id;

    @Column(name = "ID_TYPE")
    private IdType idType;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "FRONT_IMAGE_SRC")
    private String frontImageSrc;

    @Column(name = "BACK_IMAGE_SRC")
    private String backImageSrc;

    @OneToOne(mappedBy = "governmentDoc", fetch = FetchType.EAGER)
    @JsonBackReference
    private User user;

    @Column(name = "CREATED_BY",length = 50)
    private String createdBy;

    @Column(name = "CREATE_TS")
    private Date createTs = new Date();

    @Column(name = "UPDATED_BY",length = 50)
    private String updatedBy;

    @Column(name = "UPDATE_TS")
    private Date updateTs = new Date();

    public GovernmentDoc(UUID id, IdType idType, String country, User user) {
        this.id = id;
        this.idType = idType;
        this.country = country;
        this.user = user;
    }

    public GovernmentDoc(UUID id, IdType idType, String country, String frontImageSrc, String backImageSrc) {
        this.id = id;
        this.idType = idType;
        this.country = country;
        this.frontImageSrc = frontImageSrc;
        this.backImageSrc = backImageSrc;
    }
}
