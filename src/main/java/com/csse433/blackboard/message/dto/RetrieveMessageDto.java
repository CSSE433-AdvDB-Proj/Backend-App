package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * RequestParam for frontend requesting messages.
 *
 * @author chetzhang
 */
@Data
public class RetrieveMessageDto extends NotifyMessageDto{

    /**
     * The timestamp of the incoming;
     */
    private long timestamp;

    /**
     * Chat's ID. (A unique id will be assigned to a conversation when the conversation is created)
     */
    private String chatId;

    /**
     * Mark if the conversation is a group chat. Default to false;
     */
    private boolean isGroupChat = false;

}
