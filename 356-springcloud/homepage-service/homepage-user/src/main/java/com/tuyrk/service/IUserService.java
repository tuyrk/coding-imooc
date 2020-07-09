package com.tuyrk.service;

import com.tuyrk.vo.CreateUserRequest;
import com.tuyrk.vo.UserCourseInfo;
import com.tuyrk.vo.UserInfo;

/**
 * 用户相关服务接口
 *
 * @author tuyrk
 */
public interface IUserService {
    /**
     * 创建用户
     *
     * @param request 用户信息
     * @return 用户信息
     */
    UserInfo createUser(CreateUserRequest request);

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserInfo getUserInfo(Long id);

    /**
     * 获取用户和课程信息
     *
     * @param id 用户id
     * @return 用户可课程信息
     */
    UserCourseInfo getUserCourseInfo(Long id);
}
