package com.csse433.blackboard.auth.service.impl;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author chetzhang
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public UserAccountDto findUserByToken(String token) {
        String username = redisTemplate.opsForValue().get(getLoginTokenKey(token));
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setUsername("TestAccount");
        userAccountDto.setEmail("test@example.com");
        return userAccountDto;
    }

    @Override
    public void extendExpireTime(String token) {
        redisTemplate.expire(getLoginTokenKey(token), 10L, TimeUnit.MINUTES);
    }

    private String getLoginTokenKey(String token){
        return Constants.TOKEN_KEY + token;
    }
}
