package com.csse433.blackboard.pojos.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Message Table stored in MongoDB.
 *
 * @author chetzhang
 */
@Data
@Document("Message")
public class MessageEntity {

    /**
     * ID.
     */
    @Id
    private String _id;

    /**
     * Timestamp.
     */
    private Long timestamp;

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
     * Message type.
     */
    private String messageType;
}
