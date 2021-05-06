package com.csse433.blackboard.rdbms.service;

import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Chet Zhang
 * @since 2021-05-05
 */
public interface IMessageMongoBakService extends IService<MessageMongoBak> {


    List<String> getHenry(String name);
}
