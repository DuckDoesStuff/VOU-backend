package com.example.demo.controller;

import com.example.demo.dto.brand.BrandRegisterDto;
import com.example.demo.dto.brand.BrandUpdateDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.entity.BrandProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.service.BrandProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
}
