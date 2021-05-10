package com.csse433.blackboard.rdbms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Chet Zhang
 * @since 2021-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("Message_Mongo_Bak")
public class MessageMongoBak implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("content")
    private String content;

    @TableField("`from`")
    private String from;

    @TableField("timestamp")
    private Long timestamp;

    @TableField("`to`")
    private String to;

    @TableField("messageType")
    private String messageType;


}
