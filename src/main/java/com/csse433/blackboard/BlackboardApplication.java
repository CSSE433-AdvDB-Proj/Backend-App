package com.csse433.blackboard;

import com.mongodb.MongoClientSettings;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@MapperScan("com.csse433.blackboard.rdbms.mapper")
public class BlackboardApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BlackboardApplication.class, args);
        String applicationName = context.getApplicationName();
        System.out.println(applicationName + " has started successfully!");
    }

    @Bean
    MongoClientSettings mongoClientSettings() {
        return MongoClientSettings.builder().applyToClusterSettings(builder -> builder.serverSelectionTimeout(5, TimeUnit.SECONDS)).build();
    }

    @Bean
    MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(MongoProperties properties,
                                                                             Environment environment) {
        return new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
    }

}
