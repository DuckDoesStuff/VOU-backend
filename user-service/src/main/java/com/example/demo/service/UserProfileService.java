package com.example.demo.service;

import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    public UserProfile createUser(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
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
