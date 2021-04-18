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
        List<OutboundMessageVo> outbounds = messageService.getMessage(dtoList, userAccountDto);
        Map<String, List<OutboundMessageVo>> messageMap = outbounds.stream().collect(Collectors.groupingBy(OutboundMessageVo::getFrom));
        return Result.success(messageMap);
    }

}
