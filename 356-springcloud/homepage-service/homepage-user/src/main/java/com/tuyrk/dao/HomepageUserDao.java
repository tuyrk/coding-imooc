package com.tuyrk.dao;

import com.tuyrk.entity.HomepageUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * HomepageUser数据表访问接口
 *
 * @author tuyrk
 */
public interface HomepageUserDao extends JpaRepository<HomepageUser, Long> {
    /**
     * 通过用户名寻找用户记录
     *
     * @param username 用户名
     * @return 用户
     */
    HomepageUser findByUsername(String username);
}
