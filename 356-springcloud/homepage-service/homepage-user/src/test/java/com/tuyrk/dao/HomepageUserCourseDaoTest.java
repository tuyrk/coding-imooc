package com.tuyrk.dao;

import com.alibaba.fastjson.JSON;
import com.sun.tools.javac.util.List;
import com.tuyrk.HomepageUserApplicationTests;
import com.tuyrk.entity.HomepageUserCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

class HomepageUserCourseDaoTest extends HomepageUserApplicationTests {

    @Autowired
    private HomepageUserCourseDao homepageUserCourseDao;

    @Test
    @Transactional
    void findAllByUserId() {
        HomepageUserCourse userCourse1 = new HomepageUserCourse();
        userCourse1.setUserId(1L);
        userCourse1.setCourseId(1L);
        HomepageUserCourse userCourse2 = new HomepageUserCourse();
        userCourse2.setUserId(1L);
        userCourse2.setCourseId(2L);
        java.util.List<HomepageUserCourse> userCourses = homepageUserCourseDao.saveAll(List.of(userCourse1, userCourse2));
        System.out.println(JSON.toJSONString(userCourses));
        assertEquals(2, userCourses.size());
    }
}
