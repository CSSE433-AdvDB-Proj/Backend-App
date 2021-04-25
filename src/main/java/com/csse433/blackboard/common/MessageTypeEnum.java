package com.csse433.blackboard.common;

import lombok.Data;


public enum MessageTypeEnum {
    /**
     * Message.
     */
    MESSAGE,

    /**
     * Friend request.
     */
    FRIEND_REQUEST,

    /**
     * Group invitation.
     */
    GROUP_INVITATION,

    /**
     * Accept an invitation.
     */
    FRIEND_REQUEST_ACCEPTED,

    /**
     * Reject an invitation.
     */
    FRIEND_REQUEST_REJECTED;

}
