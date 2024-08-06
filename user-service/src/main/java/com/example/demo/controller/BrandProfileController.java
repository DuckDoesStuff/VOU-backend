package com.example.demo.controller;

import com.example.demo.dto.BrandRegisterDto;
import com.example.demo.dto.ProfileStateDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.entity.BrandProfile;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.service.BrandProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
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

    @PutMapping("/{brandname}")
    public BrandProfile verifyBrand(@PathVariable String brandname) {
        return brandProfileService.verifyBrand(brandname, ProfileState.VERIFIED);
    }
}
