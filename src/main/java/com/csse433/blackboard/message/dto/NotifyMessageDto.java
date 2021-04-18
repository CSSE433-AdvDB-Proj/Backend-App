package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * Notify when a new message comes.
 *
 * @author zhangx8
 */
@Data
public class NotifyMessageDto {

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
