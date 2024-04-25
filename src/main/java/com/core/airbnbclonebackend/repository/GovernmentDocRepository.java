package com.core.airbnbclonebackend.repository;

import com.core.airbnbclonebackend.entity.GovernmentDoc;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GovernmentDocRepository extends CrudRepository<GovernmentDoc, UUID> {
}
