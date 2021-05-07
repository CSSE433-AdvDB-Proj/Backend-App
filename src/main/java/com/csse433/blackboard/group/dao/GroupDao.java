package com.csse433.blackboard.group.dao;

import com.csse433.blackboard.pojos.cassandra.GroupEntity;
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
}
