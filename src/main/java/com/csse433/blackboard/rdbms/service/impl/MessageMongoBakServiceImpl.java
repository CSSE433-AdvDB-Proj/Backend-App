package com.csse433.blackboard.rdbms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.message.dto.InboundMessageDto;
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

    @Override
    public void insertTempMessage(InboundMessageDto inboundMessageDto, long time) {
        MessageMongoBak entity = convertInboundDtoToEntity(inboundMessageDto, time);
        save(entity);
    }

    @Override
    public int checkNeedToFlush() {
        return baseMapper.selectCount(new QueryWrapper<>());
    }

    private MessageMongoBak convertInboundDtoToEntity(InboundMessageDto inboundMessageDto, long time) {
        MessageMongoBak message = new MessageMongoBak();
        message.setContent(inboundMessageDto.getContent());
        message.setFrom(inboundMessageDto.getFrom());
        message.setTimestamp(time);
        message.setTo(inboundMessageDto.getTo());
        message.setMessagetype(MessageTypeEnum.MESSAGE.name());
        return message;
    }
}