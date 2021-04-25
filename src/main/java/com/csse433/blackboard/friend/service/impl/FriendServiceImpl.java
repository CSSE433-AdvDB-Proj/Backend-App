package com.csse433.blackboard.friend.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.friend.dao.FriendDao;
import com.csse433.blackboard.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author chetzhang
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private AuthService authService;

    @Autowired
    private FriendDao friendDao;

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
        if (authService.userExists(username) == null) {
            return Collections.emptyList();
        }

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
