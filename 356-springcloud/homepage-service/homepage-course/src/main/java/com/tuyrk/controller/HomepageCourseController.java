package com.tuyrk.controller;

import com.alibaba.fastjson.JSON;
import com.tuyrk.service.ICourseService;
import com.tuyrk.vo.CourseInfo;
import com.tuyrk.vo.CourseInfosRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程对外服务接口
 *
 * @author tuyrk
 */
@Slf4j
@RestController
public class HomepageCourseController {
    /**
     * 课程服务接口
     */
    private final ICourseService courseService;

    public HomepageCourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * 通过id获取课程信息
     * 不通过网关：http://127.0.0.1:7001/homepage-course/get/course?id=1
     * 通过网关：http://127.0.0.1:9000/tuyrk/homepage-course/get/course?id=1
     *
     * @param id 课程id
     * @return 课程信息
     */
    @GetMapping("/get/course")
    public CourseInfo getCourseInfo(Long id) {
        log.info("<homepage-course>:get course -> {}", id);
        return courseService.getCourseInfo(id);
    }

    /**
     * 通过ids获取课程信息
     *
     * @param request 课程id集合
     * @return 课程信息
     */
    @PostMapping("/get/courses")
    public List<CourseInfo> getCourseInfos(@RequestBody CourseInfosRequest request) {
        log.info("<homepage-course>:get courses -> {}", JSON.toJSONString(request));
        return courseService.getCourseInfos(request);
    }
}
