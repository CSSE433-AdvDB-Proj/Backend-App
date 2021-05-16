package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * Outbound message model.
 *
 * @author chetzhang
 */
@Data
public class OutboundDrawingVo {

    private OutboundDrawingVo.Content content;
    private String to;
    private String from;

    /**
     * timestamp of the message.
     */
    private long timestamp;

    @Data
    public static class Content {
        private String color;
        private int y1;
        private int x1;
        private int y0;
        private int x0;
    }

}
