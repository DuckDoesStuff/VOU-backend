package com.vou.api.service;

import com.vou.api.dto.AuthRegisterDto;
import com.vou.api.dto.response.ApiResponse;
import com.vou.api.dto.response.AuthRegisterResponse;
import com.vou.api.dto.user.UserRegisterDto;
import com.vou.api.dto.user.UserUpdateDto;
import com.vou.api.entity.UserProfile;
import com.vou.api.enumerate.ProfileState;
import com.vou.api.enumerate.Role;
import com.vou.api.exception.AuthException;
import com.vou.api.exception.ErrorCode;
import com.vou.api.exception.ProfileException;
import com.vou.api.repository.FriendRepository;
import com.vou.api.repository.UserProfileRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

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
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    KafkaService<AuthRegisterDto> kafkaAuthService;

    @Autowired
    KafkaService<String> kafkaString;

    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserById(UUID id) {
        return userProfileRepository.findById(id).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));
    }

    public UserProfile getUserByUsername(String username) {
        return userProfileRepository.findUserProfileByUsername(username).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));
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
        userProfileRepository.save(userProfile);

        // Call auth service to create auth credential and perform OTP check
        AuthRegisterDto authRegisterDto = new AuthRegisterDto(userId, userRegisterDto.getUsername(), userRegisterDto.getPassword(), userRegisterDto.getPhone(), userRegisterDto.getRole());
        kafkaAuthService.send("auth-create-topic", authRegisterDto);

        AuthRegisterResponse authRegisterResponse = new AuthRegisterResponse();
        authRegisterResponse.setName(userProfile.getUsername());
        authRegisterResponse.setPhone(userProfile.getPhone());
        authRegisterResponse.setRole(userProfile.getRole());
        authRegisterResponse.setState(userProfile.getState());
        return authRegisterResponse;
    }

    public UserProfile verifyUser(String username, ProfileState profileState) {
        Optional<UserProfile> userProfile = userProfileRepository.findUserProfileByUsername(username);
        if (userProfile.isEmpty()) throw new AuthException(ErrorCode.USER_NOT_EXIST);

        userProfile.get().setState(profileState);
        userProfileRepository.save(userProfile.get());
        return userProfile.get();
    }

    public UserProfile updateUser(UUID id, UserUpdateDto userUpdateDto) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));

        userProfile.setDisplayName(userUpdateDto.getDisplayName());
        userProfile.setPhone(userUpdateDto.getPhone());
        userProfile.setBirthday(userUpdateDto.getBirthday());
        userProfile.setEmail(userUpdateDto.getEmail());
        userProfile.setAvatar(userUpdateDto.getAvatar());
        userProfile.setGender(userUpdateDto.getGender());

        if (userProfile.getRole() != userUpdateDto.getRole() || userProfile.getState() != userUpdateDto.getState()) {
            userProfile.setRole(userUpdateDto.getRole());
            userProfile.setState(userUpdateDto.getState());
            kafkaString.send("auth-update", id + "_" + userUpdateDto.getRole() + "_" + userUpdateDto.getState());
        }

        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public String deleteUser(UUID id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));

        friendRepository.deleteAllByUser(userProfile);
        friendRepository.deleteAllByFriend(userProfile);

        userProfileRepository.deleteById(id);

        kafkaString.send("auth-delete", id.toString());

        return "Successfully deleted user";
    }
}
