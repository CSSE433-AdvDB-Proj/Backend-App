package com.csse433.blackboard.friend.dao;

import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.pojos.cassandra.FriendRelationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Columns;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author chetzhang
 */
@Component
@Slf4j
public class FriendDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public RelationTypeEnum findUserRelation(String username1, String username2) {
        Query query = Query.empty();
        query = query
                .and(Criteria.where("username1").is(username1))
                .and(Criteria.where("username2").is(username2));
        FriendRelationEntity friendRelationEntity = cassandraTemplate.selectOne(query, FriendRelationEntity.class);
        return friendRelationEntity == null ? null : friendRelationEntity.getRelation();
    }

    public void createFriendRelation(String fromUsername, String toUsername) {
        addFriendRelation(fromUsername, toUsername);
        addFriendRelation(toUsername, fromUsername);
    }

    private void addFriendRelation(String username1Value, String username2Value) {
        FriendRelationEntity friendRelationEntity = new FriendRelationEntity();
        friendRelationEntity.setUsername1(username1Value);
        friendRelationEntity.setUsername2(username2Value);
        friendRelationEntity.setGmtCreate(LocalDateTime.now());
        friendRelationEntity.setRelation(RelationTypeEnum.FRIEND);


        cassandraTemplate.insert(friendRelationEntity);
    }

    public List<String> getFriendList(String username) {
        Query query = Query.empty();

        query = query
                .columns(Columns.from("username2"))
                .and(Criteria.where("username1").is(username))
                .sort(Sort.by("username2").ascending());


        return cassandraTemplate.select(query, String.class);

    }

    public List<String> findFriendFuzzy(String currentUsername, String likeUsername) {
        Query query = Query.empty();

        query = query
                .columns(Columns.from("username2"))
                .and(Criteria.where("username1").is(currentUsername))
                .and(Criteria.where("username2").like(likeUsername))
                .sort(Sort.by("username2").ascending());


        return cassandraTemplate.select(query, String.class);

    }


    public void addFriendRequestAppendingStatus(String fromUsername, String toUsername) {
        FriendRelationEntity entity = new FriendRelationEntity();
        entity.setUsername1(fromUsername);
        entity.setUsername2(toUsername);
        entity.setGmtCreate(LocalDateTime.now());
        entity.setRelation(RelationTypeEnum.FRIEND_REQUESTING);

        cassandraTemplate.insert(entity);

    }

    public void removeRequestingRelation(String fromUsername, String toUsername) {
        Query query = Query
                .empty()
                .and(Criteria.where("username1").is(toUsername))
                .and(Criteria.where("username2").is(fromUsername));
        log.info(query.toString());
        cassandraTemplate.delete(query, FriendRelationEntity.class);
    }
}
