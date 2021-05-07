package com.csse433.blackboard.group.service;

import com.csse433.blackboard.pojos.cassandra.GroupEntity;

public interface GroupService {


    GroupEntity createGroup(String groupName, String username);

    boolean existingGroup(String groupId);

    boolean userInGroup(String username, String groupId);

    void inviteUserToGroup(String fromUsername, String toUsername, String groupId);
}
