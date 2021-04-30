package com.csse433.blackboard.friend.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;

import java.util.List;

public interface FriendService {

    /**
     * Send friend request.
     * @param fromUsername current user.
     * @param toUsername target user.
     */
    void sendFriendRequest(String fromUsername, String toUsername);

    /**
     * Generate a notification message for friend request.
     * @param fromUsername current user.
     * @param timestamp target user.
     * @return The notification message.
     */
    NotifyMessageVo generateFriendNotifyMessage(String fromUsername, long timestamp);

    /**
     * Accept or reject a friend request.
     * @param fromUsername The user who receives the friend request.
     * @param toUsername The user who sends the friend request.
     * @param accepted
     */
    void friendRequestResponse(String fromUsername, String toUsername, boolean accepted);

    /**
     * Get a list of all of your friends with detailed information.
     * @param username
     * @return
     */
    List<UserAccountDto> getFriendList(String username);

    /**
     * Fuzzy search a list of friends that likes the given String.
     * @param currentUsername
     * @param likeUsername
     * @return
     */
    List<UserAccountDto> searchFriendFuzzy(String currentUsername, String likeUsername);


}
