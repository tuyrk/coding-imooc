package com.tuyrk.blog.controller;

import com.tuyrk.blog.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Media Type 控制器
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 21:28 星期一
 * @Update : 2019/8/19 21:28 by tuyk涂元坤
 */
@RestController
public class MediaTypeController {
    /**
     * 根据客户端请求的 Content-Type，响应相应的 UserVO 类型.
     *
     * @return User 实体类
     */
    @RequestMapping("/user")
    public User getUser() {
        return new User(1L, "涂元坤", "766564616@qq.com");
    }
}
