package com.csse433.blackboard.auth.server.service.impl;

import com.csse433.blackboard.auth.server.service.RedisServerService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.protocol.ConnectionWatchdog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServerServiceImpl implements RedisServerService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static volatile boolean isConnected;

    private RedisConnectionFactory requiredConnectionFactory;

    @Value("${application.config.redis-heart-beat}")
    private int heartbeatInterval;

    {
        new Thread(() -> {
            while (true){
                if(isConnected){
                    log.info("Redis Heart Beat Check: Connected.");
                } else {
                    log.error("Redis Heart Beat Check: Reconnecting.");
                }
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(heartbeatInterval));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    redisTemplate.keys("");
                    isConnected = true;
                } catch (Exception e) {
                    isConnected = false;
                }
            }
        }).start();
    }

    @Autowired
    private RedisAutoConfiguration redisAutoConfiguration;
    @Override
    public boolean isConnected() {
        return isConnected;
    }

}
