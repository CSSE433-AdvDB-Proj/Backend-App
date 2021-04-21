package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import com.csse433.blackboard.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Message related services.
 *
 * @author chetzhang
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/getMessage")
    public Result<?> getMessage(@RequestBody List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto){
        Map<String, List<OutboundMessageVo>> messageMap = messageService.getMessage(dtoList, userAccountDto);
        return Result.success(messageMap);
    }

    @PostMapping("/getOfflineMessage")
    public Result<?> getOfflineMessage(UserAccountDto userAccountDto){
        return Result.success(messageService.getOfflineMessage(userAccountDto));
    }

}
