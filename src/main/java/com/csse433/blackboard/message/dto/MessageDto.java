package com.csse433.blackboard.message.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String from;
    private String to;
    //private String type;
    private String content;
    private long timestamp;
}
