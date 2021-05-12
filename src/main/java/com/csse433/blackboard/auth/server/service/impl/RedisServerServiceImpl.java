package com.csse433.blackboard.auth.server.service.impl;

import com.csse433.blackboard.auth.server.service.RedisServerService;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisServerServiceImpl implements RedisServerService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static volatile boolean isConnected;

    @Override
    public boolean isConnected() {
//        try {
//            redisTemplate.getRequiredConnectionFactory().getConnection();
//            return true;
//        } catch (RedisConnectionFailureException e) {
//            e.printStackTrace();
//            return false;
//        }
        return isConnected;
    }

}
