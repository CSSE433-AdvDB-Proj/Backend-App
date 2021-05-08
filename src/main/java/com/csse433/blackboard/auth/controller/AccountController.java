package com.csse433.blackboard.auth.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.auth.service.AuthService;
import com.csse433.blackboard.common.Constants;
import com.csse433.blackboard.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Result<?> updateAccountInfo(@RequestBody UserAccountDto userAccountDto,
                                       @RequestHeader(Constants.TOKEN_HEADER) String token) {
        return authService.updateUserInfo(token, userAccountDto) ? Result.success() : Result.fail("Update Failed.");
    }

    @PostMapping(value = "/logout")
    public Result<?> logout(@RequestHeader(Constants.TOKEN_HEADER) String token) {
        System.out.println("Logout called");
        authService.deleteToken(token);
        return Result.success();
    }

    @PostMapping(value = "/change_password")
    public Result<?> changePassword(UserAccountDto userAccountDto,
                                    @RequestBody Map<String,String> paramMap) {
        String password = paramMap.get("password");
        String newPassword = paramMap.get("newPassword");
        if (!authService.checkPasswordConditions(newPassword)) {
            return Result.fail("New password does not meet conditions.");
        }
        String username = userAccountDto.getUsername();
//        String password = userAccountDto.getPassword();
        if (!authService.verifyPassword(username, password)) {
            return Result.fail("Old password is incorrect.");
        }
        return authService.updateUserPassword(username, newPassword) ? Result.success() : Result.fail("Update password failed");
    }

//    @PostMapping(value = "/change_password")
//    public Result<?> changePassword(@RequestBody UserAccountDto userAccountDto,
//                                    @RequestBody String newPassword) {
//        if (!authService.checkPasswordConditions(newPassword)) {
//            return Result.fail("New password does not meet conditions.");
//        }
//        String username = userAccountDto.getUsername();
//        String password = userAccountDto.getPassword();
//        if (!authService.verifyPassword(username, password)) {
//            return Result.fail("Old password is incorrect.");
//        }
//        return authService.updateUserPassword(username, newPassword) ? Result.success() : Result.fail("Update password failed");
//    }

}
