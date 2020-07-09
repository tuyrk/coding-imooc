package com.tuyrk.client;

import com.tuyrk.vo.CourseInfo;
import com.tuyrk.vo.CourseInfosRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 通过Feign访问课程微服务
 *
 * @author tuyrk
 */
@FeignClient(value = "eureka-client-homepage-course", fallback = CourseClientHystrix.class)
public interface CourseClient {
    /**
     * 通过id获取课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @GetMapping("/homepage-course/get/course")
    CourseInfo getCourseInfo(Long id);

    /**
     * 通过ids获取课程信息
     *
     * @param request 课程id集合
     * @return 课程信息
     */
    @PostMapping("/homepage-course/get/courses")
    List<CourseInfo> getCourseInfos(CourseInfosRequest request);
}
