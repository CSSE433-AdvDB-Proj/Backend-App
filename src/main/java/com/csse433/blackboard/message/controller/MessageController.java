package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageDto;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import com.csse433.blackboard.util.TokenUtil;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;

/**
 * @author chetzhang
 */
@Controller
@Slf4j
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthService authService;

    @MessageMapping("/toUser")
    public void toUser(InboundMessageDto inboundMessageDto) {

        Date date = new Date();

        messageService.insertMessage(inboundMessageDto, date);
        NotifyMessageDto notifyMessageDto = messageService.generateNotifyMessage(inboundMessageDto, date);
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), Constants.PERSONAL_CHAT, notifyMessageDto);
    }




    @MessageMapping("/toGroup")
    public void toGroup(InboundMessageDto inboundMessageDto) {
        log.info(inboundMessageDto.toString());
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), "/response", inboundMessageDto.getContent());
        // to: groupid or sth identifies group
    }



}