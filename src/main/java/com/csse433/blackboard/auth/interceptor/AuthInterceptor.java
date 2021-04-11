package com.csse433.blackboard.auth.interceptor;

import com.alibaba.fastjson.JSON;
import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
            setReturn(response, "Login Token is null!");
            return false;
        }
        UserAccountDto user = authService.findUserByToken(token);
        if (user == null) {
            setReturn(response, "Token is invalid.");
            return false;
        }
        authService.extendExpireTime(token);
        return true;
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
