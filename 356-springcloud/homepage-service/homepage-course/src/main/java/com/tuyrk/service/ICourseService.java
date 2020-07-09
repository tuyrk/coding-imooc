package com.tuyrk.service;

import com.tuyrk.vo.CourseInfo;
import com.tuyrk.vo.CourseInfosRequest;

import java.util.List;

/**
 * 课程相关服务接口定义
 *
 * @author tuyrk
 */
public interface ICourseService {
    /**
     * 通过id获取课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    CourseInfo getCourseInfo(Long id);

    /**
     * 通过ids获取课程信息
     *
     * @param request 课程id集合
     * @return 课程信息
     */
    List<CourseInfo> getCourseInfos(CourseInfosRequest request);
}
