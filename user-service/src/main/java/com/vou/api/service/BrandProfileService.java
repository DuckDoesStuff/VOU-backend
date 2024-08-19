package com.vou.api.service;

import com.vou.api.dto.brand.BrandRegisterDto;
import com.vou.api.dto.brand.BrandUpdateDto;
import com.vou.api.dto.AuthRegisterDto;
import com.vou.api.dto.response.ApiResponse;
import com.vou.api.dto.response.AuthRegisterResponse;
import com.vou.api.entity.BrandProfile;
import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
import com.vou.api.exception.AuthException;
import com.vou.api.exception.ErrorCode;
import com.vou.api.exception.ProfileException;
import com.vou.api.repository.BrandProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandProfileService {
    public static final ParameterizedTypeReference<ApiResponse<AuthRegisterResponse>> API_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    public static final ParameterizedTypeReference<ApiResponse<String>> ERROR_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    @Autowired
    BrandProfileRepository brandProfileRepository;

    @Autowired
    KafkaService<AuthRegisterDto> kafkaAuthService;

    public List<BrandProfile> getAllBrands() {
        return brandProfileRepository.findAll();
    }

    public AuthRegisterResponse createBrand(BrandRegisterDto brandRegisterDto) {
        // Check registration info for duplicate
        if (brandProfileRepository.findBrandProfileByEmail(brandRegisterDto.getEmail()).isPresent())
            throw new AuthException(ErrorCode.EMAIL_EXISTED);
        if (brandProfileRepository.findBrandProfileByPhone(brandRegisterDto.getPhone()).isPresent())
            throw new AuthException(ErrorCode.PHONE_EXISTED);
        if (brandProfileRepository.findBrandProfileByBrandname(brandRegisterDto.getBrandname()).isPresent())
            throw new AuthException(ErrorCode.BRANDNAME_EXISTED);

        // Create user profile with "pending" state
        BrandProfile brandProfile = new BrandProfile();
        brandProfile.setEmail(brandRegisterDto.getEmail());
        brandProfile.setPhone(brandRegisterDto.getPhone());
        brandProfile.setBrandname(brandRegisterDto.getBrandname());
        brandProfile.setDisplayName(brandRegisterDto.getDisplayName());
        brandProfile.setState(ProfileState.PENDING);

        UUID brandID = UUID.randomUUID();
        brandProfile.setBrandID(brandID);
        brandProfileRepository.save(brandProfile);


        // Call auth service to create auth credential and perform OTP check
        AuthRegisterDto authRegisterDto = new AuthRegisterDto(brandID, brandProfile.getBrandname(), brandRegisterDto.getPassword(), brandProfile.getPhone(), Role.BRAND);
        kafkaAuthService.send("auth-create-topic", authRegisterDto);

        AuthRegisterResponse authRegisterResponse = new AuthRegisterResponse();
        authRegisterResponse.setName(brandProfile.getBrandname());
        authRegisterResponse.setPhone(brandProfile.getPhone());
        authRegisterResponse.setRole(Role.BRAND);
        authRegisterResponse.setState(brandProfile.getState());
        return authRegisterResponse;
    }

    public BrandProfile verifyBrand(String brandname, ProfileState profileState) {
        Optional<BrandProfile> brandProfile = brandProfileRepository.findBrandProfileByBrandname(brandname);
        if (brandProfile.isEmpty()) throw new AuthException(ErrorCode.USER_NOT_EXIST);

        brandProfile.get().setState(profileState);
        brandProfileRepository.save(brandProfile.get());
        return brandProfile.get();
    }

    public BrandProfile updateBrand(UUID id, BrandUpdateDto brandUpdateDto) {
        BrandProfile brandProfile = brandProfileRepository.findById(id)
                .orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));

        brandUpdateDto.getDisplayName().ifPresent(brandProfile::setDisplayName);
        brandUpdateDto.getPhone().ifPresent(brandProfile::setPhone);
        brandUpdateDto.getEmail().ifPresent(brandProfile::setEmail);
        brandUpdateDto.getAvatar().ifPresent(brandProfile::setAvatar);
        brandUpdateDto.getAddress().ifPresent(brandProfile::setAddress);
        brandUpdateDto.getBanner().ifPresent(brandProfile::setBanner);

        return brandProfileRepository.save(brandProfile);
    }

    public BrandProfile getBrandById(UUID id) {
        return brandProfileRepository.findById(id).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));
    }

    public BrandProfile getBrandByBrandname(String brandname) {
        return brandProfileRepository.findBrandProfileByBrandname(brandname).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));
    }
}
