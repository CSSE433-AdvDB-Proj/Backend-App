package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * RequestParam for frontend requesting messages.
 *
 * @author chetzhang
 */
@Data
public class RetrieveDrawingDto {

    /**
     * The timestamp of the incoming;
     */
    private Long timestamp;

    /**
     * Blackboard's ID.
     */
    private String boardId;

}
