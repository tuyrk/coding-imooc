package com.tuyrk.service.impl;

import com.alibaba.fastjson.JSON;
import com.tuyrk.HomepageCourseApplicationTests;
import com.tuyrk.service.ICourseService;
import com.tuyrk.vo.CourseInfo;
import com.tuyrk.vo.CourseInfosRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceImplTest extends HomepageCourseApplicationTests {

    @Autowired
    private ICourseService courseService;

    @Test
    void getCourseInfo() {
        CourseInfo courseInfo1 = courseService.getCourseInfo(1L);
        System.out.println(JSON.toJSONString(courseInfo1));
        assertEquals(1, courseInfo1.getId());
        CourseInfo courseInfo2 = courseService.getCourseInfo(2L);
        System.out.println(JSON.toJSONString(courseInfo2));
        assertEquals(2, courseInfo2.getId());
    }

    @Test
    void getCourseInfos() {
        List<CourseInfo> courseInfos = courseService.getCourseInfos(new CourseInfosRequest(Arrays.asList(1L, 2L)));
        System.out.println(JSON.toJSONString(courseInfos));
        assertEquals(2, courseInfos.size());
    }
}
