package com.csse433.blackboard.group.service.impl;

import cn.hutool.core.util.IdUtil;
import com.csse433.blackboard.group.dao.GroupDao;
import com.csse433.blackboard.group.service.GroupService;
import com.csse433.blackboard.pojos.cassandra.GroupEntity;
import com.csse433.blackboard.pojos.cassandra.UserByGroupIdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Override
    public GroupEntity createGroup(String groupName, String username) {
        String groupId = IdUtil.fastSimpleUUID();
        GroupEntity newGroup = groupDao.createNewGroup(groupId, groupName, username);
        return newGroup;
    }

    @Override
    public boolean existingGroup(String groupId) {
        GroupEntity group = groupDao.findGroupById(groupId);
        return group != null;
    }

    @Override
    public boolean userInGroup(String username, String groupId) {
        UserByGroupIdEntity user = groupDao.findUserInGroup(username, groupId);
        return user != null;
    }

    @Override
    public void inviteUserToGroup(String username, String groupId) {
        //TODO: Henry
    }
}
