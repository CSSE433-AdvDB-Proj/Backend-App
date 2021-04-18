package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * Outbound message model.
 *
 * @author chetzhang
 */
@Data
public class OutboundMessageVo {

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

    /**
     * timestamp of the message.
     */
    private long timestamp;
}
