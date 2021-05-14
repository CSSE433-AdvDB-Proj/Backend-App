package com.csse433.blackboard.auth.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse433.blackboard.auth.server.service.RedisServerService;
import com.csse433.blackboard.rdbms.service.IRedisBakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServerServiceImpl implements RedisServerService {

    @Autowired
    private volatile RedisTemplate redisTemplate;

    private static volatile boolean isConnected;

    @Value("${application.config.redis-heart-beat}")
    private int heartbeatInterval;

    @Autowired
    IRedisBakService redisBakService;

    private FlushSql commandSingleton;

    {

        new Thread(() -> {
            while(redisTemplate == null){
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
                    if(commandSingleton == null) {
                        commandSingleton = new FlushSql();
                    }

                } catch (Exception e) {
                    isConnected = false;
                    commandSingleton = null;

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


    class FlushSql{
        public FlushSql(){
            log.info("Flush Redis Bak");
            redisBakService.remove(new QueryWrapper<>());
        }
    }


}

