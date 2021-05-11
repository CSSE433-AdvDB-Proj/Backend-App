package com.csse433.blackboard.rdbms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.MessageTypeEnum;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.entity.RedisBak;
import com.csse433.blackboard.rdbms.mapper.MessageMongoBakMapper;
import com.csse433.blackboard.rdbms.mapper.RedisBakMapper;
import com.csse433.blackboard.rdbms.service.IMessageMongoBakService;
import com.csse433.blackboard.rdbms.service.IRedisBakService;
import com.csse433.blackboard.util.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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
public class RedisBakServiceImpl extends ServiceImpl<RedisBakMapper, RedisBak> implements IRedisBakService {

    @Override
    public String setUserToken(String username, String token) {
        RedisBak entity = new RedisBak();
        entity.setToken(token);
        entity.setUsername(username);
        saveOrUpdate(entity);
        return token;
    }

    @Override
    public void deleteUserToken(String token) {
        LambdaQueryWrapper<RedisBak> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(RedisBak::getToken, token);
        remove(wrapper);
    }

    @Override
    public String findUserByToken(String token) {
        LambdaQueryWrapper<RedisBak> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(RedisBak::getToken, token);
        RedisBak redisBak = baseMapper.selectOne(wrapper);
        if (!Optional.ofNullable(redisBak).isPresent()) {
            return null;
        }
        return redisBak.getUsername();
    }
}
