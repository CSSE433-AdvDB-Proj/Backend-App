package com.csse433.blackboard.auth.interceptor;

import com.alibaba.fastjson.JSON;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.error.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login interceptor.
 *
 * @author chetzhang
 */

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(Constants.TOKEN_HEADER);
        if (token == null) {
            throw GeneralException.ofNullTokenException();
        }
        UserAccountDto user = authService.findUserByToken(token);
        if (user == null) {
            throw GeneralException.ofInvalidTokenException();
        }
        authService.extendExpireTime(token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private void setReturn(HttpServletResponse response, String message) throws IOException {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //UTF-8 Encoding
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        Result failResult = Result.fail(message);
        String json = JSON.toJSONString(failResult);
        response.getWriter().print(json);
    }
}
