package com.csse433.blackboard.message.controller;

import com.alibaba.fastjson.JSON;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.message.dto.MessageDto;
import com.csse433.blackboard.util.TokenUtil;
import org.json.JSONObject;
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

import java.util.List;

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
        authService.extendExpireTime(messageDto.getToken());
        messagingTemplate.convertAndSendToUser(messageDto.getTo(), "/response", messageDto.getContent());
    }

    @MessageMapping("/toGroup")
    public void toGroup(MessageDto messageDto) {
        log.info(messageDto.toString());
//        String to = messageDto.getTo();
//        Group g = GroupService.getGroupFromId(to);
//        List<UserAccountDto> userList = g.getUserList();
//        for (UserAccountDto userAccountDto : userList) {
//            String token = TokenUtil.getTokenFromUser(userAccountDto.getUsername());
//        }
//
//        String s = JSON.toJSONString(new Object());


        messagingTemplate.convertAndSendToUser(messageDto.getTo(), "/response", messageDto.getContent());
        // to: groupid or sth identifies group
    }



}