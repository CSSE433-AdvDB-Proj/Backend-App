package com.csse433.blackboard.message.dao;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import com.csse433.blackboard.pojos.cassandra.GroupByUserEntity;
import com.csse433.blackboard.pojos.cassandra.InvitationEntity;
import com.csse433.blackboard.pojos.mongo.MessageEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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



    public List<OutboundMessageVo> getPersonalMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto) {
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

    public List<OutboundMessageVo> getGroupMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto) {
        Query query = new Query();
        query
                .addCriteria(Criteria.where("messageType").is(MessageTypeEnum.MESSAGE.name()))
                .addCriteria(Criteria.where("timestamp").is(dto.getTimestamp()))
                .addCriteria(Criteria.where("to").is(dto.getChatId()));
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
        Query friendMessageQuery = new Query();
        friendMessageQuery
                .addCriteria(Criteria.where("timestamp").gt(timestamp))
                .addCriteria(Criteria.where("to").is(userAccountDto.getUsername()));
        //Get friend messages.
        List<OutboundMessageVo> outboundMessageVos = mongoTemplate.find(friendMessageQuery, MessageEntity.class).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;

        })
                .collect(Collectors.toList());
        //Get group messages.
        List<String> groups = findGroupsByUser(username);
        Query groupMessageQuery = new Query();
        groupMessageQuery
                .addCriteria(Criteria.where("timestamp").gt(timestamp))
                .addCriteria(Criteria.where("to").in(groups));
        outboundMessageVos.addAll(mongoTemplate.find(groupMessageQuery, MessageEntity.class).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;

        })
                .collect(Collectors.toList()));

        updateLastestRetrievedTimestamp(System.currentTimeMillis(), username);


        return outboundMessageVos;

    }

    public List<String> findGroupsByUser(String username) {
        org.springframework.data.cassandra.core.query.Query query =
                org.springframework.data.cassandra.core.query.Query
                        .empty()
                        .and(org.springframework.data.cassandra.core.query.Criteria.where("username").is(username));

        return cassandraTemplate.select(query, GroupByUserEntity.class).stream().map(GroupByUserEntity::getGroupId).collect(Collectors.toList());
    }

    public List<OutboundMessageVo> getHistoryMessage(UserAccountDto userAccountDto, String from, int count, long timestamp, boolean group) {
        Query query = new Query();
        query
                .addCriteria(Criteria.where("timestamp").lt(timestamp))
                .addCriteria(Criteria.where("messageType").is(MessageTypeEnum.MESSAGE))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(count);
        if (!group) {
            String username = userAccountDto.getUsername();
            query
                    .addCriteria(Criteria.where("to").is(username))
                    .addCriteria(Criteria.where("from").is(from));
        } else {
            query.addCriteria(Criteria.where("to").is(from));
        }
        return mongoTemplate.find(query, MessageEntity.class).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;
        })
                .collect(Collectors.toList());

    }


    public List<InvitationEntity> getUnrespondedInvitations(String username) {
        org.springframework.data.cassandra.core.query.Query query =
                org.springframework.data.cassandra.core.query.Query
                        .query(org.springframework.data.cassandra.core.query.Criteria.where("to_username").is(username));
        return cassandraTemplate.select(query, InvitationEntity.class);
    }
}
