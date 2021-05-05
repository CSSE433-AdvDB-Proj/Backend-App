package com.csse433.blackboard.message.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.friend.service.impl.FriendServiceImpl;
import com.csse433.blackboard.message.dao.MessageDao;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import com.csse433.blackboard.message.service.MessageMongoService;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMongoService messageMongoService;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FriendService friendService;


    @Override
    public NotifyMessageVo generateNotifyMessage(InboundMessageDto inboundMessageDto, long timestamp) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(timestamp);
        notifyMessageVo.setChatId(inboundMessageDto.getFrom());
        notifyMessageVo.setIsGroupChat(false);
        notifyMessageVo.setType(MessageTypeEnum.MESSAGE);
        return notifyMessageVo;
    }

    @Override
    public Map<String, List<OutboundMessageVo>> getMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto) {
        List<OutboundMessageVo> outboundMessageVos = new ArrayList<>();
        dtoList = dtoList.stream().filter(dto -> friendService.isFriend(userAccountDto.getUsername(), dto.getChatId())).collect(Collectors.toList());
        for (RetrieveMessageDto dto : dtoList) {
            outboundMessageVos.addAll(messageDao.getMessage(userAccountDto, dto));
        }
        Optional<Long> max = outboundMessageVos.stream().map(OutboundMessageVo::getTimestamp).max(Long::compareTo);
        max.ifPresent(timestamp -> messageDao.updateLastestRetrievedTimestamp(timestamp, userAccountDto.getUsername()));
        return outboundMessageVos.stream().collect(Collectors.groupingBy(OutboundMessageVo::getFrom));
    }

    @Override
    public Map<String, List<OutboundMessageVo>> getOfflineMessage(UserAccountDto userAccountDto) {
        return messageDao.getOfflineMessage(userAccountDto).stream().collect(Collectors.groupingBy(OutboundMessageVo::getFrom));
    }

    @Override
    public List<OutboundMessageVo> getHistoryMessage(UserAccountDto userAccountDto, String from, int count, long fromTimestamp) {
        return messageDao.getHistoryMessage(userAccountDto, from, count, fromTimestamp);
    }

    @Override
    public void insertFriendRequestResponse(String fromUsername, String toUsername, boolean accepted, long now) {
        MessageEntity entity = new MessageEntity();
        entity.setTimestamp(now);
        entity.setFrom(fromUsername);
        entity.setTo(toUsername);
        entity.setContent("");
        entity.setMessageType(accepted ? MessageTypeEnum.FRIEND_REQUEST_ACCEPTED.name() : MessageTypeEnum.FRIEND_REQUEST_REJECTED.name());
        messageMongoService.insert(entity);
    }

    @Override
    public void insertFriendInvitation(String fromUsername, String toUsername, long now) {
        MessageEntity entity = new MessageEntity();
        entity.setTimestamp(now);
        entity.setFrom(fromUsername);
        entity.setTo(toUsername);
        entity.setContent("");
        entity.setMessageType(MessageTypeEnum.FRIEND_REQUEST.name());
        messageMongoService.insert(entity);
    }

    @Override
    public void insertMessage(InboundMessageDto inboundMessageDto, long timestamp) {
        MessageEntity entity = new MessageEntity();
        entity.setTimestamp(timestamp);
        entity.setFrom(inboundMessageDto.getFrom());
        entity.setTo(inboundMessageDto.getTo());
        entity.setContent(inboundMessageDto.getContent());
        entity.setMessageType(MessageTypeEnum.MESSAGE.name());
        messageMongoService.insert(entity);
    }



}
