package com.example.demo.service;

import com.example.demo.dto.BrandRegisterDto;
import com.example.demo.dto.UserRegisterDto;
import com.example.demo.dto.request.AuthRegisterRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.entity.BrandProfile;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.enumerate.Role;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.BrandProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
    WebClient.Builder webClientBuilder;

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
        brandProfile.setState(ProfileState.PENDING);

        UUID brandID = UUID.randomUUID();

        // Call auth API to create auth credential and perform OTP check
        WebClient webClient = webClientBuilder.build();
        String url = "http://localhost:8001/auth/register";
        AuthRegisterRequest authRegisterRequest = new AuthRegisterRequest(brandID, brandProfile.getBrandname(), brandRegisterDto.getPassword(), brandProfile.getPhone(), Role.BRAND);

        ApiResponse<AuthRegisterResponse> response;
        try {
            response = webClient.post()
                    .uri(url)
                    .body(Mono.just(authRegisterRequest), AuthRegisterRequest.class)
                    .retrieve()
                    .bodyToMono(API_RESPONSE_TYPE)
                    .block();

            brandProfileRepository.save(brandProfile);
        } catch (WebClientResponseException e) {
            ApiResponse<String> errorResponse = e.getResponseBodyAs(ERROR_RESPONSE_TYPE);

            if (errorResponse == null || e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                throw new AuthException(ErrorCode.INTERNAL);
            throw new AuthException(errorResponse.getMessage(), (HttpStatus) e.getStatusCode());
        }

        assert response != null;
        return response.getResult();
    }

    public BrandProfile verifyBrand(String brandname, ProfileState profileState) {
        Optional<BrandProfile> brandProfile = brandProfileRepository.findBrandProfileByBrandname(brandname);
        if (brandProfile.isEmpty()) throw new AuthException(ErrorCode.USER_NOT_EXIST);

        brandProfile.get().setState(profileState);
        brandProfileRepository.save(brandProfile.get());
        return brandProfile.get();
    }
}
