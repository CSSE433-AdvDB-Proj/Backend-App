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
     * All online tokens
     */
    public static final String TOKEN_POOL = "Token_Pool";

    /**
     * Login Token in Request Header
     */
    public static final String TOKEN_HEADER = "Blackboard-Token";

    /**
     * Token expire time. Default is 10 minutes.
     */
    public static final long TOKEN_EXPIRE_TIME = 100L;

    /**
     * Set expire time to be this to delete token immediately.
     */
    public static final long TOKEN_EXPIRE_IMMEDIATELY = 0L;

    /**
     * Personal chat destination address.
     */
    public static final String PERSONAL_CHAT = "/personal";

    /**
     * Group chat destination address.
     */
    public static final String GROUP_CHAT = "/group";

    /**
     * Last retrieved timestamp redis key.
     */
    public static final String LAST_RETRIEVED_TIMESTAMP_REDIS_KEY = "Last_Retrieved_Timestamp";
}
