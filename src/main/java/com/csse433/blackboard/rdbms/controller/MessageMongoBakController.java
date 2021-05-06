package com.csse433.blackboard.rdbms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.service.IMessageMongoBakService;
import com.csse433.blackboard.rdbms.service.impl.MessageMongoBakServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Chet Zhang
 * @since 2021-05-05
 */
@RestController
@RequestMapping("/messageMongoBak")
public class MessageMongoBakController {


    @Autowired
    private IMessageMongoBakService messageMongoBakService;

    @GetMapping("/get")
    public Result<?> get(){
        return Result.success(messageMongoBakService.getHenry(null));
    }

    @PostMapping("/post")
    public Result<?> get(@RequestParam String content){
        MessageMongoBak messageMongoBak = new MessageMongoBak();
        messageMongoBak.setContent(content);
        messageMongoBakService.save(messageMongoBak);
        return Result.success();
    }

}
