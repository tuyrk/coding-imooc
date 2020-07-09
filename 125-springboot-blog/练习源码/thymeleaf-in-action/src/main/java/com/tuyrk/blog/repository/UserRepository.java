package com.tuyrk.blog.repository;

import com.tuyrk.blog.domain.User;

import java.util.List;

/**
 * User Repository 接口
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 16:12 星期一
 * @Update : 2019/8/19 16:12 by tuyk涂元坤
 */
public interface UserRepository {
    /**
     * 创建或者修改用户
     *
     * @param user 用户对象
     * @return 处理后的用户对象
     */
    User saveOrUpdateUser(User user);

    /**
     * 删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Long id);

    /**
     * 获取用户列表
     * @return 用户列表
     */
    List<User> listUsers();
}
