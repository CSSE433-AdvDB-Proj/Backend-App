package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @author chetzhang
 */
@Controller
@Slf4j
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AuthService authService;

    @MessageMapping("/toUser")
    public void toUser(InboundMessageDto inboundMessageDto) {
        log.info(inboundMessageDto.toString());
        UserAccountDto user = authService.findUserByToken(inboundMessageDto.getToken());
        if (user == null) {
            log.info("token is invalid");
            return;
        }
        String usernameFromToken = user.getUsername();
        if (!inboundMessageDto.getFrom().equals(usernameFromToken)) {
            log.info("Token does not correspond to current user.");
            return;
        }
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), "/response", inboundMessageDto.getContent());
    }

    @MessageMapping("/toGroup")
    public void toGroup(InboundMessageDto inboundMessageDto) {
        log.info(inboundMessageDto.toString());
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), "/response", inboundMessageDto.getContent());
        // to: groupid or sth identifies group
    }



}