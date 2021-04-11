package com.csse433.blackboard.config;

import com.csse433.blackboard.auth.UserAccountResolver;
import com.csse433.blackboard.auth.interceptor.AuthInterceptor;
import com.csse433.blackboard.util.SpringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author chetzhang
 */
@Component
public class CustomizedWebMvcConfiguration implements WebMvcConfigurer {

    List<String> patterns = Collections.singletonList(
            "/sys/**"
    );



    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        UserAccountResolver userAccountResolver = (UserAccountResolver) SpringUtil.getBean("UserAccountResolver");
        resolvers.add(userAccountResolver);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**").excludePathPatterns(patterns);
    }


}
