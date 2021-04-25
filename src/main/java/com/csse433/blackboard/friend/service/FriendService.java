package com.csse433.blackboard.friend.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;

import java.util.List;

public interface FriendService {

    void sendFriendRequest(String fromUsername, String toUsername);

    NotifyMessageVo generateFriendNotifyMessage(String fromUsername, long timestamp);

    void friendRequestResponse(String fromUsername, String toUsername);

    List<UserAccountDto> getFriendList(String username);

    List<UserAccountDto> searchFriendFuzzy(String currentUsername, String likeUsername);


}
