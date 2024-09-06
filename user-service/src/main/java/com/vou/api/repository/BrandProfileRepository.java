package com.vou.api.repository;

import com.vou.api.entity.BrandProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface BrandProfileRepository extends JpaRepository<BrandProfile, UUID> {
    Optional<BrandProfile> findBrandProfileByEmail(String email);
    Optional<BrandProfile> findBrandProfileByBrandname(String brandname);
    Optional<BrandProfile> findBrandProfileByPhone(String phone);

    @Query("SELECT b FROM BrandProfile b WHERE b.brandname LIKE :brandname%")
    List<BrandProfile> searchByBrandname(@Param("brandname") String brandname);
}
