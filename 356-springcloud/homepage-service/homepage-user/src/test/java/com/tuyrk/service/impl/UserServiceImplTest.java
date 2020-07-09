package com.tuyrk.service.impl;

import com.alibaba.fastjson.JSON;
import com.tuyrk.HomepageUserApplicationTests;
import com.tuyrk.service.IUserService;
import com.tuyrk.vo.CreateUserRequest;
import com.tuyrk.vo.UserCourseInfo;
import com.tuyrk.vo.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest extends HomepageUserApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    @Transactional
    void createUser() {
        CreateUserRequest request = new CreateUserRequest("tuyrk", "tuyrk@qq.com");
        UserInfo user = userService.createUser(request);
        System.out.println(JSON.toJSONString(user));
        assertEquals("tuyrk", user.getUsername());
    }

    @Test
    void getUserInfo() {
        UserInfo userInfo = userService.getUserInfo(1L);
        System.out.println(JSON.toJSONString(userInfo));
        assertEquals("tuyrk", userInfo.getUsername());
    }

    // 未通过测试
    @Test
    void getUserCourseInfo() {
        UserCourseInfo userCourseInfo = userService.getUserCourseInfo(1L);
        System.out.println(JSON.toJSONString(userCourseInfo));
        assertEquals(1, userCourseInfo.getCourseInfos().size());
    }
}
