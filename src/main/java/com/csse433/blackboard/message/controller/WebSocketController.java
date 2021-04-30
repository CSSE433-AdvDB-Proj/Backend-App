package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;

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

    @MessageMapping("/toUser")
    public void toUser(InboundMessageDto inboundMessageDto)  {

        Date date = new Date();
        String fromUser = inboundMessageDto.getFrom();
        String toUser = inboundMessageDto.getTo();
        String invalidUsername = authService.userExists(fromUser, toUser);
        if (!friendService.isFriend(fromUser, toUser)) {
            log.info(fromUser + " is trying to send messages to " + toUser + " who is not one of his/her friends.");
            return;
        }
        if(StringUtils.isNotBlank(invalidUsername)){
            log.info("User not found: " + invalidUsername);
            return;
        }

        messageService.insertMessage(inboundMessageDto, date.getTime());
        NotifyMessageVo notifyMessageVo = messageService.generateNotifyMessage(inboundMessageDto, date.getTime());
        messagingTemplate.convertAndSendToUser(toUser, Constants.PERSONAL_CHAT, notifyMessageVo);
    }



    @MessageMapping("/toGroup")
    public void toGroup(InboundMessageDto inboundMessageDto) {
        log.info(inboundMessageDto.toString());
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), Constants.GROUP_CHAT, inboundMessageDto.getContent());
        // to: groupid or sth identifies group
    }




}