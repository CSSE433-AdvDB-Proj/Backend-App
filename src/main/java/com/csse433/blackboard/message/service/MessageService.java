package com.csse433.blackboard.message.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chetzhang
 */
public interface MessageService {


    void insertMessage(InboundMessageDto inboundMessageDto, long date);

    NotifyMessageVo generateNotifyMessage(InboundMessageDto inboundMessageDto, long timestamp);

    Map<String, List<OutboundMessageVo>> getPersonalMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto);

    Map<String, List<OutboundMessageVo>> getOfflineMessage(UserAccountDto userAccountDto);

    List<OutboundMessageVo> getHistoryMessage(UserAccountDto userAccountDto, String from, int count, long fromTimestamp);

    void insertFriendRequestResponse(String fromUsername, String toUsername, boolean accepted, long now);

    void insertFriendInvitation(String fromUsername, String toUsername, long now);

    void insertGroupInvitation(String fromUsername, String toUsername, long now);

    void flushTempMessage();

    NotifyMessageVo generateGroupNotifyMessage(InboundMessageDto inboundMessageDto, long time);

    Map<String,List<OutboundMessageVo>> getGroupMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto);
}
