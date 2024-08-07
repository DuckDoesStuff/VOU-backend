package com.example.demo.service;

import com.example.demo.dto.ProfileStateDto;
import com.example.demo.dto.request.AuthRegisterRequest;
import com.example.demo.dto.UserRegisterDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserProfileRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {
    public static final ParameterizedTypeReference<ApiResponse<AuthRegisterResponse>> API_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    public static final ParameterizedTypeReference<ApiResponse<String>> ERROR_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };
    @Autowired
    EntityManager entityManager;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserById(UUID id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    public AuthRegisterResponse createUser(UserRegisterDto userRegisterDto) {
        // Check registration info for duplicate
        if (userProfileRepository.findUserProfileByEmail(userRegisterDto.getEmail()).isPresent())
            throw new AuthException(ErrorCode.EMAIL_EXISTED);
        if (userProfileRepository.findUserProfileByPhone(userRegisterDto.getPhone()).isPresent())
            throw new AuthException(ErrorCode.PHONE_EXISTED);
        if (userProfileRepository.findUserProfileByUsername(userRegisterDto.getUsername()).isPresent())
            throw new AuthException(ErrorCode.USERNAME_EXISTED);

        // Create user profile with "pending" state
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(userRegisterDto.getEmail());
        userProfile.setPhone(userRegisterDto.getPhone());
        userProfile.setUsername(userRegisterDto.getUsername());
        userProfile.setRole(userRegisterDto.getRole());
        userProfile.setState(ProfileState.PENDING);

        UUID userId = UUID.randomUUID();
        userProfile.setUserID(userId);

        // Call auth API to create auth credential and perform OTP check
        WebClient webClient = webClientBuilder.build();
        String url = "http://localhost:8001/auth/register";
        AuthRegisterRequest authRegisterRequest = new AuthRegisterRequest(userId, userRegisterDto.getUsername(), userRegisterDto.getPassword(), userRegisterDto.getPhone(), userRegisterDto.getRole());

        ApiResponse<AuthRegisterResponse> response;
        try {
            response = webClient.post()
                    .uri(url)
                    .body(Mono.just(authRegisterRequest), AuthRegisterRequest.class)
                    .retrieve()
                    .bodyToMono(API_RESPONSE_TYPE)
                    .block();

            userProfileRepository.save(userProfile);
        } catch (WebClientResponseException e) {
            ApiResponse<String> errorResponse = e.getResponseBodyAs(ERROR_RESPONSE_TYPE);

            if (errorResponse == null || e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                throw new AuthException(ErrorCode.INTERNAL);
            throw new AuthException(errorResponse.getMessage(), (HttpStatus) e.getStatusCode());
        }

        assert response != null;
        return response.getResult();
    }

    public UserProfile verifyUser(String username, ProfileState profileState) {
        Optional<UserProfile> userProfile = userProfileRepository.findUserProfileByUsername(username);
        if (userProfile.isEmpty()) throw new AuthException(ErrorCode.USER_NOT_EXIST);

        userProfile.get().setState(profileState);
        userProfileRepository.save(userProfile.get());
        return userProfile.get();
    }

    public UserProfile updateUser(UUID id, UserProfile userProfileDetails) {
        UserProfile userProfile = userProfileRepository.findById(id).orElse(null);

        if (userProfile != null) {
            userProfile.setDisplayName(userProfileDetails.getDisplayName());
            userProfile.setEmail(userProfileDetails.getEmail());
            userProfile.setPhone(userProfileDetails.getPhone());
            userProfile.setRole(userProfileDetails.getRole());
            userProfile.setBirthday(userProfileDetails.getBirthday());
            userProfile.setAvatar(userProfileDetails.getAvatar());
            userProfile.setGender(userProfileDetails.getGender());
            userProfile.setFacebookAccount(userProfileDetails.getFacebookAccount());
            return userProfileRepository.save(userProfile);
        }
        return null;
    }

    public void deleteUser(UUID id) {
        userProfileRepository.deleteById(id);
    }
}
