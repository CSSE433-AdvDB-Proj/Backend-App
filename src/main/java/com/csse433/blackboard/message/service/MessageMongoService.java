package com.csse433.blackboard.message.service;

import com.csse433.blackboard.pojos.mongo.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageMongoService extends MongoRepository<MessageEntity, String> {
}
