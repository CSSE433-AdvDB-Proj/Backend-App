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
        private double y1;
        private double x1;
        private double y0;
        private double x0;
    }

}
