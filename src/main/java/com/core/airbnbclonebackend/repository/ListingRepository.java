package com.core.airbnbclonebackend.repository;

import com.core.airbnbclonebackend.entity.Listing;
import com.core.airbnbclonebackend.entity.User;
import com.core.airbnbclonebackend.projection.HostingListingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID>  {

     List<Listing> findListingByIdOrderByCreateTsAsc(UUID id);

     Optional<Listing> findListingByIdAndUser(UUID id, User user);

     @Query(value = " select " +
             " l.id as listingId, " +
             " l.title, " +
             "case when step < 18 then 'IN_PROGRESS' " +
             " when step >= 18 " +
             " and (u.is_identity_verified != '1' " +
             " or u.is_account_verified != '1' " +
             " or u.is_phone_verified != '1') then 'PENDING_FOR_VERIFICATION' " +
             " when step >= 18 " +
             " and (u.is_identity_verified = '1' " +
             " and u.is_account_verified = '1' " +
             " and u.is_phone_verified = '1') " +
             " and l.is_published != '1' then 'UNLISTED' " +
             " when step >= 18 " +
             " and (u.is_identity_verified = '1' " +
             " and u.is_account_verified = '1' " +
             " and u.is_phone_verified = '1') " +
             " and l.is_published = '1' then 'LISTED' " +
             " end as status," +
             " li.image_src as coverImage, " +
             " case when la.city is null or la.city ='' then null " +
             "      when la.city is not null then concat(la.city, ', ', la.state) " +
             " end as address, "+
             " l.create_ts "+
             " from " +
             " listing l " +
             " left join \"_user\" u on u.id = l.user_id " +
             " left join listing_images li on li.listing_id = l.id and li.is_cover_image = '1' " +
             " left join listing_address la on la.id = l.address_id " +
             " where l.user_id  =:userId order by l.create_ts asc", nativeQuery = true)
     List<HostingListingResult> findListingByUserId(UUID userId);


     @Query(value = "select " +
             " l.id as listingId, " +
             " l.title, " +
             " case when step < 18 then 'IN_PROGRESS'" +
             " when step >= 18 " +
             " and (u.is_identity_verified != '1' " +
             " or u.is_account_verified != '1' " +
             " or u.is_phone_verified != '1') then 'PENDING_FOR_VERIFICATION' " +
             " when step >= 18 " +
             " and (u.is_identity_verified = '1' " +
             " and u.is_account_verified = '1' " +
             " and u.is_phone_verified = '1') " +
             " and l.is_published != '1' then 'UNLISTED' " +
             " when step >= 18 " +
             " and (u.is_identity_verified = '1' " +
             " and u.is_account_verified = '1' " +
             " and u.is_phone_verified = '1') " +
             " and l.is_published = '1' then 'LISTED' " +
             " end as status, " +
             " li.image_src as coverImage, " +
             " case when la.city is null " +
             " or la.city = '' then null " +
             " when la.city is not null then concat(la.city,', ',la.state)" +
             " end as address, " +
             " l.create_ts "+
             " from listing l" +
             " left join _user u on u.id = l.user_id" +
             " left join listing_images li on " +
             " li.listing_id = l.id " +
             " and li.is_cover_image = '1' " +
             " left join listing_address la on " +
             " la.id = l.address_id" +
             " where l.user_id =:userId and (UPPER(la.house_number) like :keyword or UPPER(la.street) like :keyword or UPPER(la.state) like :keyword or UPPER(la.city) like :keyword or UPPER(la.postal_code) like :keyword or UPPER(l.title) like :keyword) " +
             " order by l.create_ts asc ", nativeQuery = true)
     List<HostingListingResult> findListingByKeyword(UUID userId, String keyword);
}
