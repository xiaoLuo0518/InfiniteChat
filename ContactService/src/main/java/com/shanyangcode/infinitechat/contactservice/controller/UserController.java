package com.shanyangcode.infinitechat.contactservice.controller;

import com.shanyangcode.infinitechat.contactservice.common.Result;
import com.shanyangcode.infinitechat.contactservice.data.User.User;
import com.shanyangcode.infinitechat.contactservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 16:43
 */
@RestController
@RequestMapping("api/v1/contact")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("user")
    public Result<User> getUser() {
        User user = userService.getUser();
        return Result.OK(user);
    }
}
