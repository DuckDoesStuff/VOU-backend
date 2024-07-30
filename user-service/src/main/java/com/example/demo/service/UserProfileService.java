package com.example.demo.service;

import com.example.demo.dto.ProfileStateDto;
import com.example.demo.dto.RegisterAuthDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.entity.UserProfile;
import com.example.demo.enumerate.ProfileState;
import com.example.demo.enumerate.Role;
import com.example.demo.repository.UserProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public String createUser(RegisterDto registerDto) {
        // Check registration info for duplicate

        // Create user profile with "pending" state
        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(registerDto.getEmail());
        userProfile.setPhone(registerDto.getPhone());
        userProfile.setUsername(registerDto.getUsername());
        userProfile.setRole(Role.USER);
        userProfile.setState(ProfileState.PENDING);
        userProfileRepository.save(userProfile);

        // Call auth API to create auth credential and perform OTP check
        WebClient webClient = webClientBuilder.build();
        String url = "http://localhost:8001/auth/register";
        RegisterAuthDto registerAuthDto = new RegisterAuthDto(registerDto.getUsername(), registerDto.getPassword(), registerDto.getPhone(), Role.USER);
        String result = webClient.post()
                .uri(url)
                .body(Mono.just(registerAuthDto), RegisterDto.class)
                .retrieve()
                .bodyToMono(String.class).block();

        return result;
    }

    public UserProfile changeProfileState(String username, ProfileStateDto profileStateDto) {
        UserProfile userProfile = userProfileRepository.findUserProfileByUsername(username);

        userProfile.setState(profileStateDto.getState());
        userProfileRepository.save(userProfile);
        return userProfile;
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
