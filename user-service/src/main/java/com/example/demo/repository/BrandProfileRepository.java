package com.example.demo.repository;

import com.example.demo.entity.BrandProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface BrandProfileRepository extends JpaRepository<BrandProfile, UUID> {
    Optional<BrandProfile> findBrandProfileByEmail(String email);
    Optional<BrandProfile> findBrandProfileByBrandname(String brandname);
    Optional<BrandProfile> findBrandProfileByPhone(String phone);
}
