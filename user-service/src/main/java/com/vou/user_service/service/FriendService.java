package com.vou.user_service.service;

import com.vou.user_service.entity.Friend;
import com.vou.user_service.entity.UserProfile;
import com.vou.user_service.exception.ErrorCode;
import com.vou.user_service.exception.ProfileException;
import com.vou.user_service.repository.FriendRepository;
import com.vou.user_service.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    FriendRepository friendRepository;

    private boolean alreadyFriend(List<Friend> friendList, UserProfile profile) {
        for (Friend friend : friendList) {
            if (friend.getFriend().equals(profile)) {
                return true;
            }
        }
        return false;
    }

    public Friend addFriend(String username, String friendname) {
        UserProfile userProfile = userProfileRepository.findUserProfileByUsername(username).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));
        UserProfile friendProfile = userProfileRepository.findUserProfileByUsername(friendname).orElseThrow(() -> new ProfileException(ErrorCode.PROFILE_NOT_FOUND));

        Friend myFriendShip = new Friend();
        myFriendShip.setUser(userProfile);
        myFriendShip.setFriend(friendProfile);

        Friend otherFriendShip = new Friend();
        otherFriendShip.setUser(friendProfile);
        otherFriendShip.setFriend(userProfile);

        List<Friend> myFriendList = friendRepository.findByUser(userProfile);
        List<Friend> otherFriendList = friendRepository.findByUser(friendProfile);
        if(alreadyFriend(myFriendList, friendProfile)) throw new ProfileException(ErrorCode.ALREADY_FRIEND);
        if(alreadyFriend(otherFriendList, userProfile)) throw new ProfileException(ErrorCode.ALREADY_FRIEND);

        friendRepository.save(otherFriendShip);
        return friendRepository.save(myFriendShip);
    }

    public List<Friend> getFriends(String username) {
        Optional<UserProfile> user = userProfileRepository.findUserProfileByUsername(username);
        if(user.isEmpty()) throw new ProfileException(ErrorCode.PROFILE_NOT_FOUND);
        UserProfile userProfile = user.get();
        return friendRepository.findByUser(userProfile);
    }
}
