package com.csse433.blackboard.auth.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse433.blackboard.auth.server.service.RedisServerService;
import com.csse433.blackboard.common.Command;
import com.csse433.blackboard.rdbms.service.IRedisBakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServerServiceImpl implements RedisServerService, BeanPostProcessor {

    @Autowired
    private volatile RedisTemplate<String, String> redisTemplate;

    private static volatile boolean isConnected;

    @Value("${application.config.redis-heart-beat}")
    private int heartbeatInterval;

    @Autowired
    private IRedisBakService redisBakService;

    @Autowired
    private ExecutorService executorService;

    private volatile Command commandSingleton;


    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        this.startRedisHeartBeat();
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    public void startRedisHeartBeat() {

        executorService.submit(() -> {
            while (true) {
                if (isConnected) {
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
                    if (commandSingleton == null) {
                        commandSingleton = new Command(() -> {
                            log.info("Flush Redis Bak");
                            redisBakService.remove(new QueryWrapper<>());
                        });
                    }

                } catch (Exception e) {
                    isConnected = false;
                    commandSingleton = null;

                }
            }
        });
    }
}

