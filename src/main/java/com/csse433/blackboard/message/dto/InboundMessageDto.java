package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * Incoming Message Pojo
 *
 * @author zhangx8
 */
@Data
public class InboundMessageDto {

    /**
     * The user who sends the message.
     */
    private String from;

    /**
     * The target of the message.
     */
    private String to;

    /**
     * The content of the message.
     */
    private String content;
}
