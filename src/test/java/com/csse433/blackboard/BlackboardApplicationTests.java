package com.csse433.blackboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
class BlackboardApplicationTests {


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private


    @Test
    void contextLoads() {


        try {
            redisTemplate.getRequiredConnectionFactory().getConnection();

            System.out.println(true);
        } catch (RedisConnectionFailureException e){
            System.out.println(false);
        }


    }

    @Test
    void test2(){
        while(true){
            boolean existingConnection = TransactionSynchronizationManager.hasResource(redisTemplate.getRequiredConnectionFactory());
            System.out.println(existingConnection);
        }
    }

}
