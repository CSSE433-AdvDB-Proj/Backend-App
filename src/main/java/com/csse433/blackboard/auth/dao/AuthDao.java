package com.csse433.blackboard.auth.dao;

import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.pojos.cassandra.UserEntity;
import com.csse433.blackboard.rdbms.service.IRedisBakService;
import com.csse433.blackboard.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AuthDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IRedisBakService redisBakService;

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
        String username = null;
        try {
            username = redisTemplate.opsForValue().get(TokenUtil.getLoginTokenKey(token));
        } catch (RedisConnectionFailureException | QueryTimeoutException e) {
            username = redisBakService.findUserByToken(token);
            log.error("Redis is down.");
            // e.printStackTrace();
        }
        return username;
    }

    /**
     * Refresh the token expire time.
     * @param token
     */
    public void extendTokenExpireTime(String token) {
        // TokenUtil.setTokenExpireTime(token, Constants.TOKEN_EXPIRE_TIME);
        try {
            redisTemplate.expire(TokenUtil.getLoginTokenKey(token), Constants.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
        } catch (RedisConnectionFailureException | QueryTimeoutException e) {
            log.error("Redis is down.");
        }
    }

    /**
     * Kill token.
     * @param token
     */
    public void killToken(String token){
        // redisTemplate.opsForSet().remove(Constants.TOKEN_POOL, tokenKey);
        // TokenUtil.setTokenExpireTime(token, Constants.TOKEN_EXPIRE_IMMEDIATELY);
        try {
            redisTemplate.expire(TokenUtil.getLoginTokenKey(token), Constants.TOKEN_EXPIRE_IMMEDIATELY, TimeUnit.MINUTES);
        } catch (RedisConnectionFailureException | QueryTimeoutException e) {
            log.error("Redis is down.");
            redisBakService.deleteUserToken(token);
            // e.printStackTrace();
        }
    }




    /**
     * Set a new token in Redis for a username.
     *
     * @param username
     * @param newToken
     */
    public void setNewToken(String username, String newToken) {
        // redisTemplate.opsForSet().add(Constants.TOKEN_POOL, TokenUtil.getLoginTokenKey(newToken));
        try {
            redisTemplate.opsForValue().set(TokenUtil.getLoginTokenKey(newToken), username, Constants.TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
        } catch (RedisConnectionFailureException | QueryTimeoutException e) {
            redisBakService.setUserToken(username, newToken);
            log.error("Redis is down.");
            // e.printStackTrace();
        }

    }


}
