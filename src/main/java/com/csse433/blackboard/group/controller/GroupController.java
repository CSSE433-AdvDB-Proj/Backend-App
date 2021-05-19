package com.csse433.blackboard.group.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.group.service.GroupService;
import com.csse433.blackboard.pojos.cassandra.GroupEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/group")
@CrossOrigin(originPatterns = "*", exposedHeaders = {"Blackboard-Token"}, allowedHeaders = {"Blackboard-Token"})
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/create")
    public Result<?> createGroup(@RequestParam String groupName, UserAccountDto userAccountDto){
        if(StringUtils.isBlank(groupName)){
            return Result.fail("Group Name cannot be blank.");
        }

        GroupEntity group = groupService.createGroup(groupName, userAccountDto.getUsername());
        return Result.success(group);
    }

    @GetMapping("/invite")
    public Result<?> inviteUserToGroup(@RequestParam String username,
                                       @RequestParam String groupId,
                                       UserAccountDto userAccountDto){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(groupId)) {
            return Result.fail("Username or Group Id cannot be blank.");
        }
        if(!groupService.existingGroup(groupId)){
            return Result.fail("Group does not exist.");
        }
        if(!groupService.userInGroup(userAccountDto.getUsername(), groupId)){
            return Result.fail("Inviter does not belong to the group.");
        }
        if(groupService.userInGroup(username, groupId)){
            return Result.fail("The user you invite is already in the group.");
        }
        groupService.inviteUserToGroup(userAccountDto.getUsername(), username, groupId);

        return Result.success();
    }

    @GetMapping("/respond")
    public Result<?> respondToInvitation(UserAccountDto userAccountDto,
                                         @RequestParam String inviter,
                                         @RequestParam String groupId,
                                         @RequestParam Boolean accepted) {
        if(!groupService.existingGroup(groupId)){
            return Result.fail("Group does not exist.");
        }
        if(groupService.userInGroup(userAccountDto.getUsername(), groupId)){
            return Result.fail("Already in group.");
        }
        if(!groupService.existingRequestingRelation(userAccountDto.getUsername(), inviter, groupId)){
            return Result.fail("No such invitation");
        }
        groupService.removeRequestingRelation(userAccountDto.getUsername(), inviter, groupId);
        if (accepted) {
            groupService.addUserToGroup(userAccountDto.getUsername(), groupId);
        }
        groupService.sendReponseNotifyMessage(userAccountDto.getUsername(), inviter, accepted);
        groupService.insertGroupInvitationResponse(userAccountDto.getUsername(), inviter, accepted, System.currentTimeMillis());
        return Result.success();
    }

    @GetMapping("/get_groups")
    public Result<?> getFriendList(UserAccountDto userAccountDto) {
        return Result.success(groupService.getGroupList(userAccountDto.getUsername()));
    }

    @GetMapping("/search_group")
    public Result<?> searchFriend(UserAccountDto userAccountDto,
                                  @RequestParam String groupName) {
        return Result.success(groupService.searchGroupExact(userAccountDto.getUsername(), groupName));
    }

    @GetMapping("/info")
    public Result<?> getGroupInfo(@RequestParam String groupId) {
        return Result.success(groupService.getGroupInfo(groupId));
    }
}
