package com.csse433.blackboard.common;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolve UserAccountDto from Controller Method.
 *
 * @author chetzhang
 */
@Component
public class UserAccountResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserAccountDto.class);
    }


    @Autowired
    private AuthService authService;

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String requestToken = webRequest.getHeader(Constants.TOKEN_HEADER);
        if (requestToken == null) {
            return null;
        }
        return authService.findUserByToken(requestToken);
    }


}
