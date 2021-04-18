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

    public GeneralException(){
        super();
    }

    public GeneralException(String message) {
        super(message);
    }

    public static GeneralException ofInvalidTokenException(){
        log.info("No token header found.");
        return new GeneralException("Token is invalid.");
    }

    public static GeneralException ofNullTokenException() {
        log.info("Invalid token.");
        return new GeneralException("Token is null.");
    }
}
