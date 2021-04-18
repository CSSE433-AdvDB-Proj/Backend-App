package com.csse433.blackboard.message.service;

import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.NotifyMessageDto;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

/**
 * @author chetzhang
 */
public interface MessageService {


    void insertMessage(InboundMessageDto inboundMessageDto, Date date);

    NotifyMessageDto generateNotifyMessage(InboundMessageDto inboundMessageDto, Date date);
}
