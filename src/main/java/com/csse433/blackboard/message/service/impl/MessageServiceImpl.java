package com.csse433.blackboard.message.service.impl;

import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageDto;
import com.csse433.blackboard.message.service.MessageMongoService;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMongoService messageMongoService;

    @Override
    public NotifyMessageDto generateNotifyMessage(InboundMessageDto inboundMessageDto, Date date) {
        NotifyMessageDto notifyMessageDto = new NotifyMessageDto();
        notifyMessageDto.setTimestamp(date.getTime());
        notifyMessageDto.setChatId(inboundMessageDto.getTo());
        notifyMessageDto.setGroupChat(false);
        return notifyMessageDto;
    }




    @Override
    public void insertMessage(InboundMessageDto inboundMessageDto, Date date) {
        MessageEntity entity = new MessageEntity();
        entity.setDate(date);
        entity.setFrom(inboundMessageDto.getFrom());
        entity.setTo(inboundMessageDto.getTo());
        entity.setContent(inboundMessageDto.getContent());
        messageMongoService.insert(entity);
    }
}
