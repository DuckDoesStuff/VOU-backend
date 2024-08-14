package com.example.demo.service;

import com.example.demo.entity.Friend;
import com.example.demo.entity.UserProfile;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.ProfileException;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class FriendService {
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    FriendRepository friendRepository;

    public Friend addFriend(String username, String friendname) {
        UserProfile userProfile = userProfileRepository.findUserProfileByUsername(username).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));
        UserProfile friendProfile = userProfileRepository.findUserProfileByUsername(friendname).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));

        Optional<Friend> friend = friendRepository.findByFriend(friendProfile);
        if(friend.isPresent()) throw new ProfileException(ErrorCode.ALREADY_FRIEND);

        Friend friendShip = new Friend();
        friendShip.setFriend(friendProfile);
        friendShip.setUser(userProfile);

        Friend otherFriendShip = new Friend();
        otherFriendShip.setFriend(userProfile);
        otherFriendShip.setUser(friendProfile);
        friendRepository.save(otherFriendShip);

        return friendRepository.save(friendShip);
    }

    public List<UserProfile> getFriends(String username) {
        Optional<UserProfile> user = userProfileRepository.findUserProfileByUsername(username);
        if(user.isEmpty()) throw new ProfileException(ErrorCode.PROFILE_NOT_FOUND);
        UserProfile userProfile = user.get();
        List<Friend> friends = friendRepository.findByUser(userProfile);
        return friends.stream()
                .map(Friend::getFriend)  // Extract the 'friend' field
                .distinct()  // Remove duplicates, if any
                .toList();
    }
}
