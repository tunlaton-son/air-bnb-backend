package com.core.airbnbclonebackend.entity;

import com.core.airbnbclonebackend.enums.IdType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Table(name = "GOVERNMENT_DOC")
@Entity(name = "GOVERNMENT_DOC")
@NoArgsConstructor
@Data
public class GovernmentDoc {

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

    @Column(name = "CREATED_BY",length = 50)
    private String createdBy;

    @Column(name = "CREATE_TS")
    private Date createTs = new Date();

    @Column(name = "UPDATED_BY",length = 50)
    private String updatedBy;

    @Column(name = "UPDATE_TS")
    private Date updateTs = new Date();
}
