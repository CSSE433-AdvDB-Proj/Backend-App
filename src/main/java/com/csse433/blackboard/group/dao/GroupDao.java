package com.csse433.blackboard.group.dao;

import com.csse433.blackboard.pojos.cassandra.GroupByUserEntity;
import com.csse433.blackboard.pojos.cassandra.GroupEntity;
import com.csse433.blackboard.pojos.cassandra.InvitationEntity;
import com.csse433.blackboard.pojos.cassandra.UserByGroupIdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GroupDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public GroupEntity createNewGroup(String groupId, String groupName, String username) {
        GroupEntity group = new GroupEntity();
        group.setGroupId(groupId);
        group.setGroupName(groupName);
        group.setCreateBy(username);
        group.setGmtCreate(LocalDateTime.now());

        cassandraTemplate.insert(group);

        addUserToGroup(groupId, username);
        return group;
    }

    public void addUserToGroup(String groupId, String username) {
        UserByGroupIdEntity userByGroup = new UserByGroupIdEntity();
        userByGroup.setGroupId(groupId);
        userByGroup.setUsername(username);
        userByGroup.setGmtJoin(LocalDateTime.now());
        cassandraTemplate.insert(userByGroup);

        GroupByUserEntity groupByUserEntity = new GroupByUserEntity();
        groupByUserEntity.setUsername(username);
        groupByUserEntity.setGroupId(groupId);
        cassandraTemplate.insert(groupByUserEntity);

    }

    public GroupEntity findGroupById(String groupId) {
        Query query = Query
                .empty()
                .and(Criteria.where("group_id").is(groupId));
        return cassandraTemplate.selectOne(query, GroupEntity.class);
    }

    public UserByGroupIdEntity findUserInGroup(String username, String groupId) {
        Query query = Query
                .empty()
                .and(Criteria.where("group_id").is(groupId))
                .and(Criteria.where("username").is(username));
        return cassandraTemplate.selectOne(query, UserByGroupIdEntity.class);
    }

    public List<String> findUsersFromGroup(String groupId) {
        Query query = Query
                .empty()
                .and(Criteria.where("group_id").is(groupId));
        List<UserByGroupIdEntity> userByGroupIdEntities = cassandraTemplate.select(query, UserByGroupIdEntity.class);
        return userByGroupIdEntities.stream().map(UserByGroupIdEntity::getUsername).collect(Collectors.toList());
    }

    public void createPendingInvitation(String fromUsername, String toUsername, String groupId) {
        InvitationEntity entity = new InvitationEntity();
        entity.setFromUsername(fromUsername);
        entity.setToUsername(toUsername);
        entity.setGroupId(groupId);
        entity.setIsFriendRequest(false);
        entity.setGmtCreate(LocalDateTime.now());
        cassandraTemplate.insert(entity);
    }

    public boolean existingRequestingRelation(String username, String inviter, String groupId){
        Query query = Query
                .empty()
                .and(Criteria.where("to_username").is(username))
                .and(Criteria.where("from_username").is(inviter))
                .and(Criteria.where("is_friend_request").is(false))
                .and(Criteria.where("group_id").is(groupId));
        return cassandraTemplate.count(query, InvitationEntity.class) > 0;
    }

    public boolean removeRequestingRelation(String username, String inviter, String groupId) {
        Query query = Query
                .empty()
                .and(Criteria.where("to_username").is(username))
                .and(Criteria.where("from_username").is(inviter))
                .and(Criteria.where("is_friend_request").is(false))
                .and(Criteria.where("group_id").is(groupId));
        return cassandraTemplate.delete(query, InvitationEntity.class);
    }

    public List<String> findGroupByUsername(String username) {
        Query query = Query
                .empty()
                .and(Criteria.where("username").is(username));
        List<GroupByUserEntity> groupByUserEntities = cassandraTemplate.select(query, GroupByUserEntity.class);
        return groupByUserEntities
                .stream()
                .map(GroupByUserEntity::getGroupId)
                .collect(Collectors.toList());


    }

    public GroupEntity getGroupInfo(String groupId) {
        Query query = Query
                .empty()
                .and(Criteria.where("group_id").is(groupId));
        return cassandraTemplate.selectOne(query, GroupEntity.class);
    }
}
