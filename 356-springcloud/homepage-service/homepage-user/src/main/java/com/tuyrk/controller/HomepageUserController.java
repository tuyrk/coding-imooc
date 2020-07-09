package com.tuyrk.controller;

import com.alibaba.fastjson.JSON;
import com.tuyrk.service.IUserService;
import com.tuyrk.vo.CreateUserRequest;
import com.tuyrk.vo.UserCourseInfo;
import com.tuyrk.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务对外接口
 *
 * @author tuyrk
 */
@Slf4j
@RestController
public class HomepageUserController {
    private final IUserService userService;

    public HomepageUserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public UserInfo createUser(@RequestBody CreateUserRequest request) {
        log.info("<homepage-user>: create user -> {}", JSON.toJSONString(request));
        return userService.createUser(request);
    }

    @GetMapping("/get/user")
    public UserInfo getUserInfo(Long id) {
        log.info("<homepage-user>: get user -> {}", id);
        return userService.getUserInfo(id);
    }

    @GetMapping("/get/user/course")
    public UserCourseInfo getUserCourseInfo(Long id) {
        log.info("<homepage-user>: get user course -> {}", id);
        return userService.getUserCourseInfo(id);
    }
}
