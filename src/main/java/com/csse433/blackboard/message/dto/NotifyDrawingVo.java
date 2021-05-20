package com.csse433.blackboard.message.dto;

import com.csse433.blackboard.common.MessageTypeEnum;
import lombok.Data;

/**
 * Notify when a new message comes.
 *
 * @author zhangx8
 */
@Data
public class NotifyDrawingVo {

//    private Content content;
    private String to;
    private String from;
    private String content;
//    @Data
//    public static class Content {
//        private String color;
//        private double y1;
//        private double x1;
//        private double y0;
//        private double x0;
//    }

}




