package com.csse433.blackboard.rdbms.service.impl;

import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.mapper.MessageMongoBakMapper;
import com.csse433.blackboard.rdbms.service.IMessageMongoBakService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Chet Zhang
 * @since 2021-05-05
 */
@Service
public class MessageMongoBakServiceImpl extends ServiceImpl<MessageMongoBakMapper, MessageMongoBak> implements IMessageMongoBakService {

    @Override
    public List<String> getHenry(String name) {
        return getBaseMapper().getByName(name);

    }
}
