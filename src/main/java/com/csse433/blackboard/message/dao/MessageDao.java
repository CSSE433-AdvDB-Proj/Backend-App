package com.csse433.blackboard.message.dao;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<OutboundMessageVo> getMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto) {
        Query query = new Query();
        query
                .addCriteria(Criteria.where("messageType").is(MessageTypeEnum.MESSAGE.name()))
                .addCriteria(Criteria.where("timestamp").is(dto.getTimestamp()))
                .addCriteria(Criteria.where("from").is(dto.getChatId()))
                .addCriteria(Criteria.where("to").is(userAccountDto.getUsername()));
        return mongoTemplate.find(query, MessageEntity.class).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;

        }).collect(Collectors.toList());
    }

    public void updateLastestRetrievedTimestamp(Long timestamp, String username) {
        redisTemplate.opsForHash().put(Constants.LAST_RETRIEVED_TIMESTAMP_REDIS_KEY, username, timestamp);
    }

    public List<OutboundMessageVo> getOfflineMessage(UserAccountDto userAccountDto) {
        String username = userAccountDto.getUsername();
        Long timestamp = (Long) redisTemplate.opsForHash().get(Constants.LAST_RETRIEVED_TIMESTAMP_REDIS_KEY, username);
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }
        Query query = new Query();
        query
                .addCriteria(Criteria.where("timestamp").gt(timestamp))
                .addCriteria(Criteria.where("to").is(userAccountDto.getUsername()));
        return mongoTemplate.find(query, MessageEntity.class).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;

        })
                .collect(Collectors.toList());

    }
}
