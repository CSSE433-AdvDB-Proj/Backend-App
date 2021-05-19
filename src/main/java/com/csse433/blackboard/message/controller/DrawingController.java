package com.csse433.blackboard.message.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.server.service.MongoServerService;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveDrawingDto;
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
@RequestMapping(value = "/drawing")
//@CrossOrigin(originPatterns = "*", exposedHeaders = {"Blackboard-Token"}, allowedHeaders = {"Blackboard-Token"})
public class DrawingController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MongoServerService mongoServerService;

    @PostMapping("/getDrawing")
    public Result<?> getDrawing(@RequestBody List<RetrieveDrawingDto> dtoList, UserAccountDto userAccountDto) {
        return Result.success(messageService.getDrawing(dtoList, userAccountDto));
    }

//    @PostMapping("/getHistoryDrawing")
//    public Result<?> getHistoryDrawing(UserAccountDto userAccountDto,
//                                       @RequestParam String from,
//                                       @RequestParam(required = false) Long fromTimestamp,
//                                       @RequestParam(required = false, defaultValue = "false") boolean group) {
//        //TODO: IMPL
//    }

}
