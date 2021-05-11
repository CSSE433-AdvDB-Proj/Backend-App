package com.csse433.blackboard.rdbms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Henry Yang
 * @since 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("Redis_Bak")
public class RedisBak implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("username")
    private String username;

    @TableField("token")
    private String token;

}
