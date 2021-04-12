package com.csse433.blackboard.auth.dao;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.pojos.cassandra.UserEntity;
import com.csse433.blackboard.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AuthDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean createUser(UserEntity userEntity) {
        UserEntity existingUser = getUserByUsername(userEntity.getUsername());
        if (existingUser != null) {
            return false;
        }
        cassandraTemplate.insert(userEntity);
        return true;
    }

    public boolean updateUser(UserEntity userEntity) {
        cassandraTemplate.update(userEntity);
        return true;
    }

    public UserEntity getUserByUsername(String username) {
        String cql = String.format("SELECT * FROM blackboard.user WHERE username = '%s';", username);
        return cassandraTemplate.selectOne(cql, UserEntity.class);
    }


    public String getUsernameByToken(String token) {
        return redisTemplate.opsForValue().get(TokenUtil.getLoginTokenKey(token));
    }

    public void extendExpireTime(String token) {
        redisTemplate.expire(TokenUtil.getLoginTokenKey(token), Constants.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(TokenUtil.getLoginTokenKey(token));
    }

    public void setNewToken(String username, String newToken) {
        redisTemplate.opsForValue().set(TokenUtil.getLoginTokenKey(newToken), username, Constants.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
    }
}
