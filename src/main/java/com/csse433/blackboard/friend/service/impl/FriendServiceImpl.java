package com.csse433.blackboard.friend.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.friend.dao.FriendDao;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendFriendRequest(String fromUsername, String toUsername) {
        if (authService.userExists(fromUsername, toUsername) != null) {
            return; // TODO: user not found
        }
        if (friendDao.findUserRelation(fromUsername, toUsername) != null) {
            return; // TODO: already in relation
        }
        Date date = new Date();
        NotifyMessageVo notifyMessageVo = generateFriendNotifyMessage(fromUsername, date.getTime());
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
    }

    @Override
    public NotifyMessageVo generateFriendNotifyMessage(String fromUsername, long timestamp) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(timestamp);
        notifyMessageVo.setChatId(fromUsername);
        notifyMessageVo.setIsGroupChat(false);
        notifyMessageVo.setType(MessageTypeEnum.FRIEND_REQUEST);
        return notifyMessageVo;
    }

    @Override
    public void friendRequestResponse(String fromUsername, String toUsername) {
        friendDao.createNewRelation(fromUsername, toUsername, RelationTypeEnum.FRIEND, new Date());
    }

    @Override
    public List<UserAccountDto> getFriendList(String username) {
        if (authService.userExists(username) != null) {
            return Collections.emptyList();
        }
        List<String> friendUsernames = friendDao.getFriendList(username);
        return friendUsernames.stream().map(friendUsername -> authService.getUserFromUsername(friendUsername)).collect(Collectors.toList());
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
