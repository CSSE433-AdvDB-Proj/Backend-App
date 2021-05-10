package com.csse433.blackboard.group.service;

import com.csse433.blackboard.pojos.cassandra.GroupEntity;

import java.util.List;

public interface GroupService {


    GroupEntity createGroup(String groupName, String username);

    boolean existingGroup(String groupId);

    boolean userInGroup(String username, String groupId);

    void inviteUserToGroup(String fromUsername, String toUsername, String groupId);

    List<String> findUsersFromGroup(String groupId);

    void addUserToGroup(String username, String groupId);

    void sendReponseNotifyMessage(String fromUsername, String toUsername, boolean accepted);

    boolean removeRequestingRelation(String username, String inviter, String groupId);

    boolean existingRequestingRelation(String username, String inviter, String groupId);

    void insertGroupInvitationResponse(String fromUsername, String toUsername, boolean accepted, long now);
}
