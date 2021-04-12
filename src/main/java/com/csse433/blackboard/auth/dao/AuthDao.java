package com.csse433.blackboard.auth.dao;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.pojos.cassandra.UserEntity;
import com.csse433.blackboard.util.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.catalina.User;
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

    /**
     * Inserts a user in cassandra. Returns false if the username exists.
     *
     * @param userEntity
     * @return
     */
    public boolean createUser(UserEntity userEntity){
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

    /**
     * Find a user with the username.
     *
     * @param username
     * @return
     */
    public UserEntity getUserByUsername(String username){
        String cql = String.format("SELECT * FROM blackboard.user WHERE username = '%s';", username);
        return cassandraTemplate.selectOne(cql, UserEntity.class);
    }

    /**
     * Get the username associated with the given token in Redis.
     *
     * @param token
     * @return
     */
    public String getUsernameByToken(String token) {
        return redisTemplate.opsForValue().get(TokenUtil.getLoginTokenKey(token));
    }

    /**
     * Refresh the token expire time.
     * @param token
     */
    public void extendTokenExpireTime(String token) {
        TokenUtil.setTokenExpireTime(token, Constants.TOKEN_EXPIRE_TIME);
    }

    /**
     * Kill token.
     * @param token
     */
    public void killToken(String token){
        TokenUtil.setTokenExpireTime(token, Constants.TOKEN_EXPIRE_IMMEDIATELY);
    }




    /**
     * Set a new token in Redis for a username.
     *
     * @param username
     * @param newToken
     */
    public void setNewToken(String username, String newToken) {
        redisTemplate.opsForValue().set(TokenUtil.getLoginTokenKey(newToken), username, Constants.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
    }
}
