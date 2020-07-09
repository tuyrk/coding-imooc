package com.tuyrk.blog.repository;

import com.tuyrk.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * User Repository 接口
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 16:12 星期一
 * @Update : 2019/8/19 16:12 by tuyk涂元坤
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
