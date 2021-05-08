package com.csse433.blackboard.auth.controller;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import com.csse433.blackboard.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wrong")
public class TestController {

    @GetMapping("/no")
    public Result<?> test(UserAccountDto userAccountDto){
        return Result.success();
    }
}
