package com.csse433.blackboard.pojos.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Message Table stored in MongoDB.
 *
 * @author chetzhang
 */
@Data
public class MessageEntity {

    /**
     * ID.
     */
    @Id
    private String _id;

    /**
     * Timestamp.
     */
    private Date date;

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
