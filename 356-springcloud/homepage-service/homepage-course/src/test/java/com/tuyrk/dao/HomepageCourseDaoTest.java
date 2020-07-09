package com.tuyrk.dao;

import com.tuyrk.HomepageCourseApplicationTests;
import com.tuyrk.entity.HomepageCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

class HomepageCourseDaoTest extends HomepageCourseApplicationTests {

    @Autowired
    private HomepageCourseDao homepageCourseDao;

    @Test
    @Transactional
    public void testCreateCourseInfo() {
        HomepageCourse course1 = new HomepageCourse(
                "JDK11&12 新特性解读",
                0,
                "https://www.imooc.com",
                "解读 JDK11 和 JDK12 的新版本特性"
        );
        HomepageCourse course2 = new HomepageCourse(
                "基于 SpringCloud 微服务架构 广告系统设计与实现",
                1,
                "https://www.imooc.com",
                "广告系统的设计与实现"
        );
        List<HomepageCourse> homepageCourses = homepageCourseDao.saveAll(Arrays.asList(course1, course2));
        assertEquals(2, homepageCourses.size());
    }
}
