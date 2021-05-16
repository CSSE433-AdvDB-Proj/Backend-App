package com.csse433.blackboard.message.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.message.dto.*;

import java.util.List;
import java.util.Map;

/**
 * @author chetzhang
 */
public interface MessageService {


    void insertMessage(InboundMessageDto inboundMessageDto, long date);

    NotifyMessageVo generateNotifyMessage(InboundMessageDto inboundMessageDto, long timestamp);

    NotifyMessageVo generateNotifyDrawing(InboundDrawingDto inboundDrawingDto, long timestamp);

    Map<String, List<OutboundMessageVo>> getPersonalMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto);

    Map<String, List<OutboundMessageVo>> getOfflineMessage(UserAccountDto userAccountDto);

    List<OutboundMessageVo> getHistoryMessage(UserAccountDto userAccountDto, String from, int count, long fromTimestamp, boolean group);

    void insertFriendRequestResponse(String fromUsername, String toUsername, boolean accepted, long now);

    void insertFriendInvitation(String fromUsername, String toUsername, long now);

    void insertGroupInvitation(String fromUsername, String toUsername, long now);

    void flushTempMessage();

    NotifyMessageVo generateGroupNotifyMessage(InboundMessageDto inboundMessageDto, long time);

    Map<String,List<OutboundMessageVo>> getGroupMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto);

    void insertDrawing(InboundDrawingDto inboundDrawingDto, long time);

    Map<String, List<OutboundDrawingVo>> getDrawing(List<RetrieveDrawingDto> dtoList, UserAccountDto userAccountDto);
}
