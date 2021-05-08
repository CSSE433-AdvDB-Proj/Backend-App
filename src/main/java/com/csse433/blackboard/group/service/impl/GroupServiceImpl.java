package com.csse433.blackboard.group.service.impl;

import cn.hutool.core.util.IdUtil;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.group.dao.GroupDao;
import com.csse433.blackboard.group.service.GroupService;
import com.csse433.blackboard.message.dto.NotifyMessageVo;
import com.csse433.blackboard.message.service.MessageService;
import com.csse433.blackboard.pojos.cassandra.GroupEntity;
import com.csse433.blackboard.pojos.cassandra.UserByGroupIdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

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
    public void inviteUserToGroup(String fromUsername, String toUsername, String groupId) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(System.currentTimeMillis());
        notifyMessageVo.setChatId(fromUsername);
        notifyMessageVo.setIsGroupChat(false);
        notifyMessageVo.setType(MessageTypeEnum.GROUP_INVITATION);
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
        //Store message to Mongo.
        Date date = new Date();
        messageService.insertGroupInvitation(fromUsername, toUsername, date.getTime());

        groupDao.createPendingInvitation(fromUsername, toUsername, groupId);
    }

    @Override
    public List<String> findUsersFromGroup(String groupId) {
        return groupDao.findUsersFromGroup(groupId);
    }

    @Override
    public void addUserToGroup(String username, String groupId) {
        groupDao.addUserToGroup(groupId, username);
    }

    @Override
    public void sendReponseNotifyMessage(String fromUsername, String toUsername, boolean accepted) {
        NotifyMessageVo notifyMessageVo = new NotifyMessageVo();
        notifyMessageVo.setTimestamp(System.currentTimeMillis());
        notifyMessageVo.setChatId(fromUsername);
        notifyMessageVo.setIsGroupChat(false);
        notifyMessageVo.setType(accepted ? MessageTypeEnum.GROUP_INVITATION_ACCEPTED : MessageTypeEnum.GROUP_INVITATION_REJECTED);
        messagingTemplate.convertAndSendToUser(toUsername, Constants.PERSONAL_CHAT, notifyMessageVo);
    }

    @Override
    public boolean removeRequestingRelation(String username, String inviter, String groupId) {
        return groupDao.removeRequestingRelation(username, inviter, groupId);
    }
}
