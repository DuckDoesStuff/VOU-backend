package com.example.demo.service;

import com.example.demo.dto.ProfileStateDto;
import com.example.demo.dto.request.AuthRegisterRequest;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.response.AuthRegisterResponse;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.enumerate.Role;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserProfileRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {
    @Autowired
    EntityManager entityManager;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    public AuthRegisterResponse createUser(RegisterDto registerDto) {
        // Check registration info for duplicate
        if (userProfileRepository.findUserProfileByEmail(registerDto.getEmail()).isPresent())
            throw new AuthException(ErrorCode.EMAIL_EXISTED);
        if (userProfileRepository.findUserProfileByPhone(registerDto.getPhone()).isPresent())
            throw new AuthException(ErrorCode.PHONE_EXISTED);
        if (userProfileRepository.findUserProfileByUsername(registerDto.getUsername()).isPresent())
            throw new AuthException(ErrorCode.USERNAME_EXISTED);

        // Create user profile with "pending" state
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(registerDto.getEmail());
        userProfile.setPhone(registerDto.getPhone());
        userProfile.setUsername(registerDto.getUsername());
        userProfile.setRole(registerDto.getRole());
        userProfile.setState(ProfileState.PENDING);
        userProfileRepository.save(userProfile);


        // Call auth API to create auth credential and perform OTP check
        WebClient webClient = webClientBuilder.build();
        String url = "http://localhost:8001/auth/register";
        AuthRegisterRequest authRegisterRequest = new AuthRegisterRequest(userProfile.getUserID(), registerDto.getUsername(), registerDto.getPassword(), registerDto.getPhone(), registerDto.getRole());

        AuthRegisterResponse result = webClient.post()
                .uri(url)
                .body(Mono.just(authRegisterRequest), RegisterDto.class)
                .retrieve()
                .bodyToMono(AuthRegisterResponse.class)
                .block();

        return result;
    }

    public UserProfile changeProfileState(String username, ProfileStateDto profileStateDto) {
        Optional<UserProfile> userProfile = userProfileRepository.findUserProfileByUsername(username);
        if(userProfile.isEmpty()) throw new AuthException(ErrorCode.USER_NOT_EXIST);

        userProfile.get().setState(profileStateDto.getState());
        userProfileRepository.save(userProfile.get());
        return userProfile.get();
    }

    public UserProfile updateUser(Long id, UserProfile userProfileDetails) {
        UserProfile userProfile = userProfileRepository.findById(id).orElse(null);

        if (userProfile != null) {
            userProfile.setNameOfUser(userProfileDetails.getNameOfUser());
            userProfile.setEmail(userProfileDetails.getEmail());
            userProfile.setPhone(userProfileDetails.getPhone());
            userProfile.setRole(userProfileDetails.getRole());
            userProfile.setActivate(userProfileDetails.isActivate());
            userProfile.setBirthday(userProfileDetails.getBirthday());
            userProfile.setAvatar(userProfileDetails.getAvatar());
            userProfile.setGender(userProfileDetails.getGender());
            userProfile.setFacebookAccount(userProfileDetails.getFacebookAccount());
            return userProfileRepository.save(userProfile);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userProfileRepository.deleteById(id);
    }

    public UserProfile activateUser(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElse(null);
        if (userProfile != null) {
            userProfile.setActivate(true);
            return userProfileRepository.save(userProfile);
        }
        return null;
    }

    public UserProfile deactivateUser(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElse(null);
        if (userProfile != null) {
            userProfile.setActivate(false);
            return userProfileRepository.save(userProfile);
        }
        return null;
    }
}
