package com.csse433.blackboard.message.dto;

import lombok.Data;

/**
 * Incoming Message Pojo
 *
 * @author zhangx8
 */
@Data
public class InboundDrawingDto {

    private Content content;
    private String to;
    private String from;


    @Data
    public static class Content {
        private String color;
        private double y1;
        private double x1;
        private double y0;
        private double x0;
    }
}
