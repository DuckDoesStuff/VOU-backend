package com.vou.api.controller;

import com.vou.api.dto.brand.BrandRegisterDto;
import com.vou.api.dto.brand.BrandUpdateDto;
import com.vou.api.dto.request.ActivateProfileRequest;
import com.vou.api.dto.response.ApiResponse;
import com.vou.api.dto.response.AuthRegisterResponse;
import com.vou.api.entity.BrandProfile;
import com.vou.api.enumerate.ProfileState;
import com.vou.api.service.BrandProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile/brand")
public class BrandProfileController {
    @Autowired
    BrandProfileService brandProfileService;

    @GetMapping
    public ApiResponse<List<BrandProfile>> getAllBrands() {
        return ApiResponse.<List<BrandProfile>>builder()
                .code(200)
                .result(brandProfileService.getAllBrands())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<BrandProfile>> getBrandMatchBrandname(@RequestParam("brandname") String brandname) {
        return ApiResponse.<List<BrandProfile>>builder()
                .code(200)
                .result(brandProfileService.getBrandMatchBrandname(brandname))
                .build();
    }

    @PostMapping
    public ApiResponse<AuthRegisterResponse> createBrand(@Valid @RequestBody BrandRegisterDto brandRegisterDto) {
        return ApiResponse.<AuthRegisterResponse>builder()
                .result(brandProfileService.createBrand(brandRegisterDto))
                .message("Successfully created brand profile")
                .code(200)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandProfile> getBrandById(@PathVariable UUID id) {
        return ApiResponse.<BrandProfile>builder()
                .result(brandProfileService.getBrandById(id))
                .code(200)
                .build();
    }

    @PatchMapping("/{brandname}")
    public BrandProfile verifyBrand(@PathVariable String brandname) {
        return brandProfileService.verifyBrand(brandname, ProfileState.VERIFIED);
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandProfile> updateBrandProfile(@RequestBody BrandUpdateDto brandUpdateDto, @PathVariable UUID id) {
        return ApiResponse.<BrandProfile>builder()
                .code(200)
                .result(brandProfileService.updateBrand(id, brandUpdateDto))
                .build();
    }

    @KafkaListener(topics = "brand-profile-topic")
    public void listener(ActivateProfileRequest activateProfileRequest) {
        brandProfileService.verifyBrand(activateProfileRequest.getName(), activateProfileRequest.getProfileState());
    }
}
