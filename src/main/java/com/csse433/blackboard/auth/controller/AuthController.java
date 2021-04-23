package com.csse433.blackboard.auth.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

/**
 * @author chetzhang
 * @author henryyang
 */
@RestController
@RequestMapping(value = "/sys")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

//    @CrossOrigin(originPatterns = "*")
    @PostMapping(value = "/register")
    public Result<?> register(@RequestBody UserAccountDto userAccountDto, HttpServletResponse response) {
        Field[] fields = userAccountDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldValue = (String) field.get(userAccountDto);
                if (StringUtils.isBlank(fieldValue)) {
                    log.info(field.getName() + " can't be empty!");
                    return Result.fail(field.getName() + " can't be empty!");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        boolean registerStatus = authService.registerUser(userAccountDto);
        if (registerStatus) {
            return Result.success();
        } else {
            log.info("Existing username.");
            return Result.fail("Existing username.");
        }

    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody UserAccountDto userAccountDto, HttpServletResponse response) {
        String username = userAccountDto.getUsername();
        String password = userAccountDto.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Result.fail("Please provide credentials.");
        }

        boolean success = authService.login(username, password, response);
        if (success) {
            return Result.success();
        }
        return Result.fail("Invalid credentials.");
    }

}
