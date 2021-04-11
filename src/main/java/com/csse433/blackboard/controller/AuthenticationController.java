package com.csse433.blackboard.controller;

import com.csse433.blackboard.common.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody String username,
                        @RequestBody String password) {
        System.out.println(username + password);
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {

        return null;
    }

}
