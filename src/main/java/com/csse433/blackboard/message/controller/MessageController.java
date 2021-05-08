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
@RequestMapping(value = "/message")
//@CrossOrigin(originPatterns = "*", exposedHeaders = {"Blackboard-Token"}, allowedHeaders = {"Blackboard-Token"})
public class MessageController {

    @Autowired
    private MessageService messageService;


    @PostMapping("/getMessage")
    public Result<?> getMessage(@RequestBody List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto) {
        Map<Boolean, List<RetrieveMessageDto>> mapByChatType = dtoList.stream().collect(Collectors.groupingBy(RetrieveMessageDto::getIsGroupChat));
        Map<String, List<OutboundMessageVo>> messageMap = messageService.getPersonalMessage(mapByChatType.get(false), userAccountDto);
        messageMap.putAll(messageService.getGroupMessage(mapByChatType.get(true), userAccountDto));

        return Result.success(messageMap);
    }

    @PostMapping("/getOfflineMessage")
    public Result<?> getOfflineMessage(UserAccountDto userAccountDto) {
        return Result.success(messageService.getOfflineMessage(userAccountDto));
    }

    @GetMapping("/history_message")
    public Result<?> getHistoryMessage(UserAccountDto userAccountDto,
                                       @RequestParam int messageCount,
                                       @RequestParam String from,
                                       @RequestParam(required = false) Long fromTimestamp,
                                       @RequestParam(required = false, defaultValue = "false") boolean group) {

        return Result.success(messageService.getHistoryMessage(
                userAccountDto,
                from,
                messageCount,
                fromTimestamp == null ? System.currentTimeMillis() : fromTimestamp,
                group));
    }

}
