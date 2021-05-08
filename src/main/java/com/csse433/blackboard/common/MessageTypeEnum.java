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
     * Accept a group invitation.
     */
    GROUP_INVITATION_ACCEPTED,

    /**
     * Reject a group invitation.
     */
    GROUP_INVITATION_REJECTED,

    /**
     * Accept an invitation.
     */
    FRIEND_REQUEST_ACCEPTED,

    /**
     * Reject an invitation.
     */
    FRIEND_REQUEST_REJECTED;

}
