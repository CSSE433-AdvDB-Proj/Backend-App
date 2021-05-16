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
        private int y1;
        private int x1;
        private int y0;
        private int x0;
    }
}
