package com.csse433.blackboard.message.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;

import java.util.Date;
import java.util.List;

/**
 * @author chetzhang
 */
public interface MessageService {


    void insertMessage(InboundMessageDto inboundMessageDto, long date);

    NotifyMessageVo generateNotifyMessage(InboundMessageDto inboundMessageDto, long timestamp);

    List<OutboundMessageVo> getMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto);
}
