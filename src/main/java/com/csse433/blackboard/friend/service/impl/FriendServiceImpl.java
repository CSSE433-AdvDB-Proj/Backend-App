package com.csse433.blackboard.friend.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.friend.service.FriendService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chetzhang
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Override
    public void sendFriendRequest(String fromUsername, String toUsername) {
        //TODO: Henry
    }

    @Override
    public void friendRequestResponse(String fromUsername, String toUsername) {
        //TODO: Henry
    }

    @Override
    public List<UserAccountDto> getFriendList(String username) {
        return null;
    }

    @Override
    public UserAccountDto searchUsername(String username) {
        return null;
    }

    @Override
    public List<UserAccountDto> searchFriendFuzzy(String likeUsername) {
        return null;
    }
}
