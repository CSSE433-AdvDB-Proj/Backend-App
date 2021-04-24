package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private AuthService authService;

    @ResponseBody
    @GetMapping("/friend/addFriend")
    public Result<?> sendFriendRequest(@RequestParam String username, UserAccountDto userAccountDto){
        //TODO: addFriendRequest
//        messagingTemplate.convertAndSendToUser();
        return Result.success();
    }

    @MessageMapping("/toUser")
    public void toUser(InboundMessageDto inboundMessageDto)  {

        Date date = new Date();
        String invalidUsername = authService.userExists(inboundMessageDto.getFrom(), inboundMessageDto.getTo());
        if(StringUtils.isNotBlank(invalidUsername)){
            log.info("User not found: " + invalidUsername);
            return;
        }

        messageService.insertMessage(inboundMessageDto, date.getTime());
        NotifyMessageVo notifyMessageVo = messageService.generateNotifyMessage(inboundMessageDto, date.getTime());
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), Constants.PERSONAL_CHAT, notifyMessageVo);
    }



    @MessageMapping("/toGroup")
    public void toGroup(InboundMessageDto inboundMessageDto) {
        log.info(inboundMessageDto.toString());
        messagingTemplate.convertAndSendToUser(inboundMessageDto.getTo(), Constants.GROUP_CHAT, inboundMessageDto.getContent());
        // to: groupid or sth identifies group
    }




}