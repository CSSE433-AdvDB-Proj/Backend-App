package com.csse433.blackboard.friend.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.friend.dao.FriendDao;
import com.csse433.blackboard.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<String> friendUsernames = friendDao.getFriendList(username);
        return friendUsernames.stream().map(friendUsername -> authService.getUserFromUsername(friendUsername)).collect(Collectors.toList());
    }

    @Override
    public List<UserAccountDto> searchFriendFuzzy(String currentUsername, String likeUsername) {
        List<String> fuzzyFriendUsernames = friendDao.findFriendFuzzy(currentUsername, likeUsername);
        return fuzzyFriendUsernames.stream().map(friendUsername -> authService.getUserFromUsername(friendUsername)).collect(Collectors.toList());
    }
}
