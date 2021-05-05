package com.csse433.blackboard.config;

import com.csse433.blackboard.auth.interceptor.AuthInterceptor;
import com.csse433.blackboard.common.UserAccountResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author chetzhang
 */
@Component
public class CustomizedWebMvcConfiguration implements WebMvcConfigurer {

    List<String> patterns = Arrays.asList(
            "/sys/**",
            "/error",
            "/messageMongoBak/**"
//            "/wrong/**"
    );


    @Autowired
    private UserAccountResolver userAccountResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userAccountResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").exposedHeaders("Blackboard-Token", "Content-Type").allowedHeaders("Blackboard-Token", "Content-Type");
//        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns(patterns);
    }


}
