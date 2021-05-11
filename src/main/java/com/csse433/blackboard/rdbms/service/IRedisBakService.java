package com.csse433.blackboard.rdbms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.message.dto.InboundMessageDto;
import com.csse433.blackboard.message.dto.OutboundMessageVo;
import com.csse433.blackboard.message.dto.RetrieveMessageDto;
import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.entity.RedisBak;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Henry Yang
 * @since 2021-05-11
 */
public interface IRedisBakService extends IService<RedisBak> {

    String setUserToken(String username, String token);

    void deleteUserToken(String token);

    String findUserByToken(String token);

    // void insertTempMessage(InboundMessageDto inboundMessageDto, long time);

    // int messageCacheCount();

    // List<OutboundMessageVo> getPersonalMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto);

    // List<OutboundMessageVo> getGroupMessage(UserAccountDto userAccountDto, RetrieveMessageDto dto);
}
