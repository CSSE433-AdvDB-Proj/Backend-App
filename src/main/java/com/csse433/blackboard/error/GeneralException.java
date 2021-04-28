package com.csse433.blackboard.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * General error class for this application.
 *
 * @author chetzhang
 */
@Slf4j
public class GeneralException extends RuntimeException{

//    @Autowired
//    private ErrorLogService errorLogService;


    public GeneralException(String message) {
        super(message);
    }


    public static GeneralException ofUserNotFoundException(String username) {
        return new GeneralException("User not found: " + username);
    }

    public static GeneralException ofRepeatFriendRequestException(String toUsername) {
        return new GeneralException("You have already became a friend with: " + toUsername + ".");
    }
}
