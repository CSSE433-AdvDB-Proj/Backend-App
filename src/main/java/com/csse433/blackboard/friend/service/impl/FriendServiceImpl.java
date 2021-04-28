package com.csse433.blackboard.friend.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.error.GeneralException;
import com.csse433.blackboard.friend.dao.FriendDao;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.service.MessageService;
import com.sun.tools.javah.Gen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    private MessageService messageService;

    @Override
    public void sendFriendRequest(String fromUsername, String toUsername) {
        if (authService.userExists(toUsername) != null) {
            throw GeneralException.ofUserNotFoundException(toUsername);
        }
        if (friendDao.findUserRelation(fromUsername, toUsername) != null) {
            throw GeneralException.ofRepeatFriendRequestException(toUsername);
        }
        Date date = new Date();
        NotifyMessageVo notifyMessageVo = generateFriendNotifyMessage(fromUsername, date.getTime());
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
        messageService.insertFriendInvitation(fromUsername, toUsername, date.getTime());
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
    public void friendRequestResponse(String fromUsername, String toUsername, boolean accepted) {
        if(accepted){
            friendDao.createNewRelation(fromUsername, toUsername, RelationTypeEnum.FRIEND, new Date());
        }
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        long now = System.currentTimeMillis();
        notifyMessageVo.setTimestamp(now);
        notifyMessageVo.setChatId(fromUsername);
        notifyMessageVo.setType(accepted ? MessageTypeEnum.FRIEND_REQUEST_ACCEPTED : MessageTypeEnum.FRIEND_REQUEST_REJECTED);
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
        messageService.insertFriendRequestResponse(fromUsername, toUsername, accepted, now);
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
