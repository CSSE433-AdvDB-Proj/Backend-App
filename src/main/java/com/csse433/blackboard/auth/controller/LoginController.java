package com.csse433.blackboard.auth.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chetzhang
 * @author henryyang
 */
@RestController
@RequestMapping(value = "/sys")
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody UserAccountDto userAccountDto) {

        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {

        return null;
    }
}
