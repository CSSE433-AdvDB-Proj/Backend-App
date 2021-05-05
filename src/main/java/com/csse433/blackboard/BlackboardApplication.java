package com.csse433.blackboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.csse433.blackboard.rdbms.mapper")
public class BlackboardApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BlackboardApplication.class, args);
        String applicationName = context.getApplicationName();
        System.out.println(applicationName + " has started successfully!");
    }

}
