package com.core.airbnbclonebackend.repository;

import com.core.airbnbclonebackend.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID>  {


}
