package com.csse433.blackboard.auth.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

/**
 * @author henryyang
 */
@RestController
@RequestMapping(value = "/account")
@Slf4j
public class AccountController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/update")
    public Result<?> updateUserInfo(@RequestBody UserAccountDto userAccountDto, HttpServletResponse response) {
        Field[] fields = userAccountDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldValue = (String) field.get(userAccountDto);
                if(StringUtils.isBlank(fieldValue)){
                    log.info(field.getName() + " can't be empty!");
                    return Result.fail(field.getName() + " can't be empty!");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        boolean registerStatus = authService.registerUser(userAccountDto);
        if(registerStatus){
            return Result.success();
        } else {
            log.info("Existing username.");
            return Result.fail("Existing username.");
        }

    }

    @PostMapping(value = "/logout")
    public Result<?> logout(@RequestHeader(Constants.TOKEN_HEADER) String token) {
        authService.deleteToken(token);
        return Result.success();
    }

}
