package com.csse433.blackboard.error;

import com.csse433.blackboard.common.RelationTypeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * General error class for this application.
 *
 * @author chetzhang
 */
@Slf4j
public class GeneralException extends RuntimeException{


    public GeneralException(String message) {
        super(message);
    }


    public static GeneralException ofUserNotFoundException(String username) {
        return new GeneralException("User not found: " + username);
    }

    public static GeneralException ofSameUsernameFriendRequestException() {
        return new GeneralException("You cannot add yourself as friend");
    }

    public static GeneralException ofRepeatFriendRequestException(RelationTypeEnum userRelation, String toUsername) {
        log.info(userRelation.name() + " " + toUsername);
        if(userRelation.equals(RelationTypeEnum.FRIEND)){
            return new GeneralException("You have already became a friend with: " + toUsername + ".");
        } else if(userRelation.equals(RelationTypeEnum.FRIEND_REQUESTING)){
            return new GeneralException("You have already requested to become a friend with: " + toUsername + ".");
        }
        return new GeneralException("Friend request error!");
    }

    public static GeneralException ofInvalidOperationException() {
        return new GeneralException("Invalid Operation.");
    }
}
