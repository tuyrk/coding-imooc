package com.tuyrk.blog.repository.impl;

import com.tuyrk.blog.domain.User;
import com.tuyrk.blog.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User Repository 实现类
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 16:16 星期一
 * @Update : 2019/8/19 16:16 by tuyk涂元坤
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private static AtomicLong counter = new AtomicLong();
    /**
     * 存储用户信息的容器
     */
    private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

    /**
     * 创建或者修改用户
     *
     * @param user 用户对象
     * @return 处理后的用户对象
     */
    @Override
    public User saveOrUpdateUser(User user) {
        Long id = user.getId();
        // 用户ID为空，则新建用户
        if (id == null) {
            id = counter.incrementAndGet();
            user.setId(id);
        }
        this.userMap.put(id, user);
        return user;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @Override
    public void deleteUser(Long id) {
        this.userMap.remove(id);
    }

    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    @Override
    public User getUserById(Long id) {
        return this.userMap.get(id);
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @Override
    public List<User> listUsers() {
        return new ArrayList<> (this.userMap.values());
    }
}
