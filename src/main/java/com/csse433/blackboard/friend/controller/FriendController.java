package com.csse433.blackboard.friend.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller Handling Friend requests
 *
 * @author henryyang
 */

@RestController
@RequestMapping(value = "/friend")
@CrossOrigin(originPatterns = "*", exposedHeaders = {"Blackboard-Token"}, allowedHeaders = {"Blackboard-Token"})
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/send")
    public Result<?> sendFriendRequest(UserAccountDto userAccountDto,
                                       @RequestParam String toUsername) {
        friendService.sendFriendRequest(userAccountDto.getUsername(), toUsername);
        return Result.success();
    }

    @GetMapping("/respond")
    public Result<?> respondToFriendRequest(UserAccountDto userAccountDto,
                                            @RequestParam String toUsername,
                                            @RequestParam boolean accepted) {
        friendService.friendRequestResponse(userAccountDto.getUsername(), toUsername, accepted);
        return Result.success();
    }

    @GetMapping("/get_friends")
    public Result<?> getFriendList(UserAccountDto userAccountDto) {
        return Result.success(friendService.getFriendList(userAccountDto.getUsername()));
    }

    @GetMapping("/search_friend")
    public Result<?> searchFriend(UserAccountDto userAccountDto,
                                  @RequestParam String likeUsername) {
        return Result.success(friendService.searchFriendFuzzy(userAccountDto.getUsername(), likeUsername));
    }

}
