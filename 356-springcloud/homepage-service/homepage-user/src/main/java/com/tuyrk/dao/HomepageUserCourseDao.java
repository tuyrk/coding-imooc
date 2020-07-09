package com.tuyrk.dao;

import com.tuyrk.entity.HomepageUserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * HomepageUserCourse数据表访问接口
 *
 * @author tuyuankun
 */
public interface HomepageUserCourseDao extends JpaRepository<HomepageUserCourse, Long> {
    /**
     * 通过用户id寻找数据记录
     *
     * @param userId 用户id
     * @return 用户课程信息
     */
    List<HomepageUserCourse> findAllByUserId(Long userId);
}
