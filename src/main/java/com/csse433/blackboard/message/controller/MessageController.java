package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.message.dto.MessageDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.mail.Message;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    public void toUser(MessageDto messageDto) {
        log.info(messageDto.toString());
        UserAccountDto user = authService.findUserByToken(messageDto.getToken());
        if (user == null) {
            log.info("token is invalid");
            return;
        }
        String usernameFromToken = user.getUsername();
        if (!messageDto.getFrom().equals(usernameFromToken)) {
            log.info("Token does not correspond to current user.");
            return;
        }
        messagingTemplate.convertAndSendToUser(messageDto.getTo(), "/response", messageDto.getContent());
    }

    @MessageMapping("/toGroup")
    public void toGroup(MessageDto messageDto) {
        log.info(messageDto.toString());
        messagingTemplate.convertAndSendToUser(messageDto.getTo(), "/response", messageDto.getContent());
        // to: groupid or sth identifies group
    }



}