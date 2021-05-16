package com.csse433.blackboard.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.server.service.MongoServerService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.friend.service.FriendService;
import com.csse433.blackboard.group.service.GroupService;
import com.csse433.blackboard.message.dao.MessageDao;
import com.csse433.blackboard.message.dto.*;
import com.csse433.blackboard.message.service.MessageMongoService;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.pojos.cassandra.InvitationEntity;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.service.IMessageMongoBakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMongoService messageMongoService;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private FriendService friendService;

    @Autowired
    private IMessageMongoBakService messageBakService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MongoServerService mongoServerService;


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
    public Map<String, List<OutboundMessageVo>> getPersonalMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto) {
        boolean connected = mongoServerService.isFirstServerConnected();
        List<OutboundMessageVo> outboundMessageVos = new ArrayList<>();
        if (dtoList != null) {
            dtoList = dtoList.stream().filter(dto -> friendService.isFriend(userAccountDto.getUsername(), dto.getChatId())).collect(Collectors.toList());
            for (RetrieveMessageDto dto : dtoList) {
                if(connected) {
                    outboundMessageVos.addAll(messageDao.getPersonalMessage(userAccountDto, dto));
                } else {
                    //Potential Bugs
                    outboundMessageVos.addAll(messageBakService.getPersonalMessage(userAccountDto, dto));
                }
            }
            Optional<Long> max = outboundMessageVos.stream().map(OutboundMessageVo::getTimestamp).max(Long::compareTo);
            max.ifPresent(timestamp -> messageDao.updateLastestRetrievedTimestamp(timestamp, userAccountDto.getUsername()));
        }
        return outboundMessageVos.stream().collect(Collectors.groupingBy(OutboundMessageVo::getFrom));
    }

    @Override
    public Map<String, List<OutboundMessageVo>> getGroupMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto) {
        boolean connected = mongoServerService.isFirstServerConnected();
        List<OutboundMessageVo> outboundMessageVos = new ArrayList<>();
        if (dtoList != null) {
            dtoList = dtoList.stream().filter(dto -> groupService.userInGroup(userAccountDto.getUsername(), dto.getChatId())).collect(Collectors.toList());
            for (RetrieveMessageDto dto : dtoList) {
                if(connected) {
                    outboundMessageVos.addAll(messageDao.getGroupMessage(userAccountDto, dto));
                } else {
                    outboundMessageVos.addAll(messageBakService.getGroupMessage(userAccountDto, dto));
                }
            }
            Optional<Long> max = outboundMessageVos.stream().map(OutboundMessageVo::getTimestamp).max(Long::compareTo);
            max.ifPresent(timestamp -> messageDao.updateLastestRetrievedTimestamp(timestamp, userAccountDto.getUsername()));
        }
        return outboundMessageVos.stream().collect(Collectors.groupingBy(OutboundMessageVo::getFrom));
    }

    @Override
    public void insertDrawing(InboundDrawingDto inboundDrawingDto, long timestamp) {
        MessageEntity entity = new MessageEntity();
        entity.setTimestamp(timestamp);
        entity.setFrom(inboundDrawingDto.getFrom());
        entity.setTo(inboundDrawingDto.getTo());
        entity.setContent(JSON.toJSONString(inboundDrawingDto.getContent()));
        entity.setMessageType(MessageTypeEnum.DRAWING.name());
        messageMongoService.insert(entity);
    }

    @Override
    public Map<String, List<OutboundMessageVo>> getOfflineMessage(UserAccountDto userAccountDto) {
        //Query unresponded invitations and notify.
        List<InvitationEntity> invitations = messageDao.getUnrespondedInvitations(userAccountDto.getUsername());
        List<NotifyMessageVo> invitationNotifications = generateInvitationNotifications(invitations);
        invitationNotifications.forEach(notifyVo -> messagingTemplate.convertAndSendToUser(userAccountDto.getUsername(), Constants.PERSONAL_CHAT, notifyVo));
        //Query offline messages.
        return messageDao.getOfflineMessage(userAccountDto).stream().collect(Collectors.groupingBy(OutboundMessageVo::getFrom));
    }

    private List<NotifyMessageVo> generateInvitationNotifications(List<InvitationEntity> invitations) {
        return invitations.stream().map(this::invitationToNotification).collect(Collectors.toList());
    }

    private NotifyMessageVo invitationToNotification(InvitationEntity invitationEntity) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(invitationEntity.getGmtCreate().toInstant(ZoneOffset.UTC).toEpochMilli());
        notifyMessageVo.setChatId(invitationEntity.getFromUsername());
        notifyMessageVo.setIsGroupChat(false);
        notifyMessageVo.setType(invitationEntity.getIsFriendRequest() ? MessageTypeEnum.FRIEND_REQUEST : MessageTypeEnum.GROUP_INVITATION);
        return notifyMessageVo;

    }

    @Override
    public List<OutboundMessageVo> getHistoryMessage(UserAccountDto userAccountDto, String from, int count, long fromTimestamp, boolean group) {
        return messageDao.getHistoryMessage(userAccountDto, from, count, fromTimestamp, group);
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
    public void insertGroupInvitation(String fromUsername, String toUsername, long now) {
        MessageEntity entity = new MessageEntity();
        entity.setTimestamp(now);
        entity.setFrom(fromUsername);
        entity.setTo(toUsername);
        entity.setContent("");
        entity.setMessageType(MessageTypeEnum.GROUP_INVITATION.name());
        messageMongoService.insert(entity);
    }

    @Override
    public synchronized void flushTempMessage() {
        int count = messageBakService.messageCacheCount();
        if (count != 0) {
            messageCacheToMongo(count);
            log.info("MySQL messages have been flushed to MongoDB successfully.");
        }

    }

    private void messageCacheToMongo(int count) {
        for (int offset = 0; offset < count; offset += 20) {
            QueryWrapper<MessageMongoBak> wrapper = new QueryWrapper<MessageMongoBak>().last(String.format("LIMIT %s, %s", offset, 20)).orderByAsc("timestamp");
            List<MessageMongoBak> messages = messageBakService
                    .getBaseMapper()
                    .selectList(wrapper);
            List<MessageEntity> toMongo = messages.stream().map(this::sqlMessageToMongoMessage).collect(Collectors.toList());
            messageMongoService.saveAll(toMongo);
        }
        messageBakService.getBaseMapper().delete(new QueryWrapper<>());

    }

    private MessageEntity sqlMessageToMongoMessage(MessageMongoBak bakMessage){
        MessageEntity mongoMessage = new MessageEntity();
        BeanUtils.copyProperties(bakMessage, mongoMessage);
        return mongoMessage;
    }

    @Override
    public NotifyMessageVo generateGroupNotifyMessage(InboundMessageDto inboundMessageDto, long time) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(time);
        notifyMessageVo.setChatId(inboundMessageDto.getTo());
        notifyMessageVo.setIsGroupChat(true);
        notifyMessageVo.setType(MessageTypeEnum.MESSAGE);
        return notifyMessageVo;
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
