package com.csse433.blackboard.message.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMongoService messageMongoService;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public NotifyMessageVo generateNotifyMessage(InboundMessageDto inboundMessageDto, long timestamp) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(timestamp);
        notifyMessageVo.setChatId(inboundMessageDto.getTo());
        notifyMessageVo.setIsGroupChat(false);
        return notifyMessageVo;
    }

    @Override
    public List<OutboundMessageVo> getMessage(List<RetrieveMessageDto> dtoList, UserAccountDto userAccountDto) {
        List<OutboundMessageVo> outboundMessageVos = new ArrayList<>();
        for (RetrieveMessageDto dto : dtoList) {
            outboundMessageVos.addAll(messageDao.getMessage(userAccountDto, dto));
        }
        return outboundMessageVos;
    }



    @Override
    public void insertMessage(InboundMessageDto inboundMessageDto, long timestamp) {
        MessageEntity entity = new MessageEntity();
        entity.setTimestamp(timestamp);
        entity.setFrom(inboundMessageDto.getFrom());
        entity.setTo(inboundMessageDto.getTo());
        entity.setContent(inboundMessageDto.getContent());
        messageMongoService.insert(entity);
    }
}
