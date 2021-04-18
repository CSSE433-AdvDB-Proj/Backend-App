package com.csse433.blackboard.message.dto;

import lombok.Data;

@Data
public class MessageDto {
    private String from;
    private String to;
    private String content;
    private long timestamp;
    private String token;
}
