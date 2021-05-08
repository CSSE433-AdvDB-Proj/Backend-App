package com.csse433.blackboard.friend.dao;

import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.pojos.cassandra.FriendRelationEntity;
import com.csse433.blackboard.pojos.cassandra.InvitationEntity;
import jnr.ffi.annotations.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.query.Columns;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chetzhang
 */
@Component
@Slf4j
public class FriendDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    /**
     * Find the user relation. A relation can be any of the {@code RelationTypeEnum}
     *
     * @param username1
     * @param username2
     * @return RelationTypeEnum, or null.
     * @see RelationTypeEnum
     */
    public RelationTypeEnum findUserRelation(String username1, String username2) {
        Query query = Query.empty();
        query = query
                .and(Criteria.where("username1").is(username1))
                .and(Criteria.where("username2").is(username2));
        FriendRelationEntity friendRelationEntity = cassandraTemplate.selectOne(query, FriendRelationEntity.class);
        return friendRelationEntity == null ? null : friendRelationEntity.getRelation();
    }

    /**
     * Make both users as friends.
     *
     * @param fromUsername
     * @param toUsername
     */
    public void createFriendRelation(String fromUsername, String toUsername) {
        addFriendRelation(fromUsername, toUsername);
        addFriendRelation(toUsername, fromUsername);
    }

    /**
     * Add user2 as a friend of user1.
     *
     * @param username1Value
     * @param username2Value
     */
    private void addFriendRelation(String username1Value, String username2Value) {
        FriendRelationEntity friendRelationEntity = new FriendRelationEntity();
        friendRelationEntity.setUsername1(username1Value);
        friendRelationEntity.setUsername2(username2Value);
        friendRelationEntity.setGmtCreate(LocalDateTime.now());
        friendRelationEntity.setRelation(RelationTypeEnum.FRIEND);


        cassandraTemplate.insert(friendRelationEntity);
    }

    /**
     * Query a list of friend that matches the provided username.
     *
     * @param username
     * @return
     */
    public List<String> getFriendList(String username) {
        Query query = Query.empty();

        query = query
                .columns(Columns.from("username2"))
                .and(Criteria.where("username1").is(username))
                .sort(Sort.by("username2").ascending());


        return cassandraTemplate
                .select(query, FriendRelationEntity.class)
                .stream()
                .map(FriendRelationEntity::getUsername2)
                .collect(Collectors.toList());

    }


    /**
     * Fuzzy search a list of friends that has username like the provided username.
     *
     * @param currentUsername
     * @param likeUsername
     * @return
     */
    public List<String> findFriendFuzzy(String currentUsername, String likeUsername) {
        Query query = Query.empty();

        query = query
                .columns(Columns.from("username2"))
                .and(Criteria.where("username1").is(currentUsername))
                .and(Criteria.where("username2").like(likeUsername));
                //.sort(Sort.by("username2").ascending());


        return cassandraTemplate
                .select(query, FriendRelationEntity.class)
                .stream()
                .map(FriendRelationEntity::getUsername2)
                .sorted()
                .collect(Collectors.toList());

    }


    /**
     * Add user2 a pending friend of user1.
     *
     * @param fromUsername
     * @param toUsername
     */
    public void addFriendRequestAppendingStatus(String fromUsername, String toUsername) {
//        FriendRelationEntity entity = new FriendRelationEntity();
//        entity.setUsername1(fromUsername);
//        entity.setUsername2(toUsername);
//        entity.setGmtCreate(LocalDateTime.now());
//        entity.setRelation(RelationTypeEnum.FRIEND_REQUESTING);
//
//        cassandraTemplate.insert(entity);

        InvitationEntity entity = new InvitationEntity();
        entity.setFromUsername(fromUsername);
        entity.setToUsername(toUsername);
        entity.setIsFriendRequest(true);
        entity.setGmtCreate(LocalDateTime.now());
        cassandraTemplate.insert(entity);

    }

    /**
     * Remove any relation from user1 to user2.
     *
     * @param fromUsername
     * @param toUsername
     * @return
     */
    public boolean removeRequestingRelation(String fromUsername, String toUsername) {
//        Query query = Query
//                .empty()
//                .and(Criteria.where("username1").is(toUsername))
//                .and(Criteria.where("username2").is(fromUsername));
//        log.info(query.toString());
//        return cassandraTemplate.delete(query, FriendRelationEntity.class);

        Query query = Query
                .empty()
                .and(Criteria.where("to_username").is(fromUsername))
                .and(Criteria.where("from_username").is(toUsername))
                .and(Criteria.where("is_friend_request").is(true));
        log.info(query.toString());
        return cassandraTemplate.delete(query, InvitationEntity.class);
    }

}
