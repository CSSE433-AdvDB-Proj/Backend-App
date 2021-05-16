package com.csse433.blackboard.rdbms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.message.dto.*;
import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.mapper.MessageMongoBakMapper;
import com.csse433.blackboard.rdbms.service.IMessageMongoBakService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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
    public int messageCacheCount() {
        return baseMapper.selectCount(new QueryWrapper<>());
    }

    @Override
    public List<OutboundMessageVo> getPersonalMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto) {
        LambdaQueryWrapper<MessageMongoBak> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(MessageMongoBak::getMessageType, MessageTypeEnum.MESSAGE.name())
                .eq(MessageMongoBak::getTimestamp, dto.getTimestamp())
                .eq(MessageMongoBak::getFrom, dto.getChatId())
                .eq(MessageMongoBak::getTo, userAccountDto.getUsername());
        return baseMapper.selectList(wrapper).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;

        }).collect(Collectors.toList());
    }

    @Override
    public List<OutboundMessageVo> getGroupMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto) {
        LambdaQueryWrapper<MessageMongoBak> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(MessageMongoBak::getMessageType, MessageTypeEnum.MESSAGE.name())
                .eq(MessageMongoBak::getTimestamp, dto.getTimestamp())
                .eq(MessageMongoBak::getTo, dto.getChatId());
        return baseMapper.selectList(wrapper).stream().map(in -> {
            OutboundMessageVo out = new OutboundMessageVo();
            BeanUtils.copyProperties(in, out);
            return out;

        }).collect(Collectors.toList());
    }

    @Override
    public void insertTempDrawing(InboundDrawingDto inboundDrawingDto, long time) {
        MessageMongoBak message = new MessageMongoBak();
        message.setContent(JSON.toJSONString(inboundDrawingDto.getContent()));
        message.setFrom(inboundDrawingDto.getFrom());
        message.setTimestamp(time);
        message.setTo(inboundDrawingDto.getTo());
        message.setMessageType(MessageTypeEnum.DRAWING.name());
        save(message);
    }

    @Override
    public List<OutboundDrawingVo> getDrawing(UserAccountDto userAccountDto, RetrieveDrawingDto dto) {
        LambdaQueryWrapper<MessageMongoBak> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(MessageMongoBak::getMessageType, MessageTypeEnum.DRAWING.name())
                .eq(MessageMongoBak::getTimestamp, dto.getTimestamp())
                .eq(MessageMongoBak::getTo, dto.getBoardId());
        return baseMapper.selectList(wrapper).stream().map(in -> {
            OutboundDrawingVo out = new OutboundDrawingVo();
            out.setContent(JSON.parseObject(in.getContent(), OutboundDrawingVo.Content.class));
            out.setFrom(in.getFrom());
            out.setTo(in.getTo());
            out.setTimestamp(in.getTimestamp());
            return out;
        }).collect(Collectors.toList());
    }

    private MessageMongoBak convertInboundDtoToEntity(InboundMessageDto inboundMessageDto, long time) {
        MessageMongoBak message = new MessageMongoBak();
        message.setContent(inboundMessageDto.getContent());
        message.setFrom(inboundMessageDto.getFrom());
        message.setTimestamp(time);
        message.setTo(inboundMessageDto.getTo());
        message.setMessageType(MessageTypeEnum.MESSAGE.name());
        return message;
    }
}
