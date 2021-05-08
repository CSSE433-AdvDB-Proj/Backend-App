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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${application.config.allow-no-friend-communication}")
    private boolean allowNoFriendCommunication;

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
        if(fromUsername.equals(toUsername)) {
            throw GeneralException.ofSameUsernameFriendRequestException();
        }
        if (authService.userExists(toUsername) != null) {
            throw GeneralException.ofUserNotFoundException(toUsername);
        }
        RelationTypeEnum userRelation = friendDao.findUserRelation(fromUsername, toUsername);
        if (userRelation != null) {
            throw GeneralException.ofRepeatFriendRequestException(userRelation, toUsername);
        }
        Date date = new Date();
        NotifyMessageVo notifyMessageVo = generateFriendNotifyMessage(fromUsername, date.getTime());
        //Add friend request pending relationships for both users.
        friendDao.addFriendRequestAppendingStatus(fromUsername, toUsername);
        //Send to websocket.
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
        //Store message to Mongo.
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
        Date now = new Date();
        //Check if the target user have sent friend request.
        if(!friendDao.existingRequestingRelation(fromUsername, toUsername)){
            throw GeneralException.ofInvalidOperationException();
        }

        if (!friendDao.removeRequestingRelation(fromUsername, toUsername)) {
            throw GeneralException.ofInvalidOperationException();
        }
        //Create friend relationship on acception.
        if(accepted){
            friendDao.createFriendRelation(fromUsername, toUsername);
        }
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();

        notifyMessageVo.setTimestamp(now.getTime());
        notifyMessageVo.setChatId(fromUsername);
        notifyMessageVo.setType(accepted ? MessageTypeEnum.FRIEND_REQUEST_ACCEPTED : MessageTypeEnum.FRIEND_REQUEST_REJECTED);
        //Notify target user.
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
        //Log message.
        messageService.insertFriendRequestResponse(fromUsername, toUsername, accepted, now.getTime());
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

    @Override
    public boolean isFriend(String username, String target) {
        return allowNoFriendCommunication || RelationTypeEnum.FRIEND.equals(friendDao.findUserRelation(username, target));
    }
}
