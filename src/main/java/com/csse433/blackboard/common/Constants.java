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

    /**
     * Token expire time. Default is 10 minutes.
     */
    public static final long TOKEN_EXPIRE_TIME = 10L;

    /**
     * Set expire time to be this to delete token immediately.
     */
    public static final long TOKEN_EXPIRE_IMMEDIATELY = 0L;
}
