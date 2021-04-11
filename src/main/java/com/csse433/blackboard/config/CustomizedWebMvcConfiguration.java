package com.csse433.blackboard.config;

import com.csse433.blackboard.common.UserAccountResolver;
import com.csse433.blackboard.auth.interceptor.AuthInterceptor;
import com.csse433.blackboard.util.SpringUtil;
import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author chetzhang
 */
@Component
public class CustomizedWebMvcConfiguration implements WebMvcConfigurer {

    List<String> patterns = Arrays.asList(
            "/sys/**",
            "/error"
//            "/wrong/**"
    );



    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        UserAccountResolver userAccountResolver = (UserAccountResolver) SpringUtil.getBean("UserAccountResolver");
        resolvers.add(userAccountResolver);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AuthInterceptor authInterceptor = (AuthInterceptor) SpringUtil.getBean("AuthInterceptor");
        Objects.requireNonNull(authInterceptor);
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns(patterns);
    }


}
