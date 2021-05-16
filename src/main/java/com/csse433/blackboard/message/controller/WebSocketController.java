package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.server.service.MongoServerService;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.group.service.GroupService;
import com.csse433.blackboard.message.dto.InboundDrawingDto;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.rdbms.service.IMessageMongoBakService;
import com.mongodb.client.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chetzhang
 */
@Controller
@Slf4j
public class WebSocketController {




    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private AuthService authService;

    @Autowired
    private IMessageMongoBakService messageBakService;

    @Autowired
    private MongoServerService mongoServerService;

    @MessageMapping("/toUser")
    public void toUser(InboundMessageDto inboundMessageDto) {

        Date date = new Date();
        String fromUser = inboundMessageDto.getFrom();
        String toUser = inboundMessageDto.getTo();
        String invalidUsername = authService.userExists(fromUser, toUser);
        if (!friendService.isFriend(fromUser, toUser)) {
            log.info(fromUser + " is trying to send messages to " + toUser + " who is not one of his/her friends.");
            return;
        }
        if (StringUtils.isNotBlank(invalidUsername)) {
            log.info("User not found: " + invalidUsername);
            return;
        }

        if (mongoServerService.isFirstServerConnected()) {
            messageService.insertMessage(inboundMessageDto, date.getTime());
            flush();
        } else {
            messageBakService.insertTempMessage(inboundMessageDto, date.getTime());
        }
        NotifyMessageVo notifyMessageVo = messageService.generateNotifyMessage(inboundMessageDto, date.getTime());
        messagingTemplate.convertAndSendToUser(toUser, Constants.PERSONAL_CHAT, notifyMessageVo);
    }


    @Autowired
    private GroupService groupService;

    @MessageMapping("/toGroup")
    public void toGroup(InboundMessageDto inboundMessageDto) {
        Date date = new Date();
        String fromUser = inboundMessageDto.getFrom();
        String toGroup = inboundMessageDto.getTo();
        String invalidUsername = authService.userExists(fromUser);
        if (!groupService.existingGroup(toGroup)) {
            log.info(fromUser + " is trying to send messages to " + toGroup + " which does not exist.");
            return;
        }
        if (!groupService.userInGroup(fromUser, toGroup)) {
            log.info(fromUser + " is trying to send messages to " + toGroup + " where the user does not belong to.");
            return;
        }

        if (StringUtils.isNotBlank(invalidUsername)) {
            log.trace("User not found: " + invalidUsername);
            return;
        }

        if (mongoServerService.isFirstServerConnected()) {
            messageService.insertMessage(inboundMessageDto, date.getTime());
            flush();
        } else {
            messageBakService.insertTempMessage(inboundMessageDto, date.getTime());
        }
        NotifyMessageVo notifyMessageVo = messageService.generateGroupNotifyMessage(inboundMessageDto, date.getTime());
        List<String> usernames = groupService.findUsersFromGroup(toGroup).stream().filter(username -> !username.equals(fromUser)).collect(Collectors.toList());
        usernames.forEach(username -> messagingTemplate.convertAndSendToUser(username, Constants.GROUP_CHAT, notifyMessageVo));
    }

    @MessageMapping("/toBoard")
    public void toGroup(InboundDrawingDto inboundDrawingDto) {
        System.out.println(inboundDrawingDto);
    }

    private void flush(){
        new Thread(() -> messageService.flushTempMessage()).start();
    }


}