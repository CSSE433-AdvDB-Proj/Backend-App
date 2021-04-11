package com.csse433.blackboard.common;

/**
 * Global constants go here.
 *
 * @author chetzhang
 */
public class Constants {

    /**
     * Login token in Redis
     */
    public static final String TOKEN_KEY = "Auth:Token:";

    /**
     * Login Token in Request Header
     */
    public static final String TOKEN_HEADER = "Blackboard-Token";

    public static final long TOKEN_EXPIRE_TIME = 10L;
}
