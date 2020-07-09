package com.tuyrk.service.impl;

import com.tuyrk.dao.HomepageCourseDao;
import com.tuyrk.entity.HomepageCourse;
import com.tuyrk.service.ICourseService;
import com.tuyrk.vo.CourseInfo;
import com.tuyrk.vo.CourseInfosRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 课程服务功能实现
 *
 * @author tuyrk
 */
@Slf4j
@Service
public class CourseServiceImpl implements ICourseService {
    private final HomepageCourseDao homepageCourseDao;

    public CourseServiceImpl(HomepageCourseDao homepageCourseDao) {
        this.homepageCourseDao = homepageCourseDao;
    }

    /**
     * 通过id获取课程信息
     *
     * @param id 课程id
     * @return 课程信息
     */
    @Override
    public CourseInfo getCourseInfo(Long id) {
        Optional<HomepageCourse> course = homepageCourseDao.findById(id);
        return buildCourseInfo(course.orElse(HomepageCourse.invalid()));
    }

    /**
     * 通过ids获取课程信息
     *
     * @param request 课程id集合
     * @return 课程信息
     */
    @Override
    public List<CourseInfo> getCourseInfos(CourseInfosRequest request) {
        if (CollectionUtils.isEmpty(request.getIds())) {
            return Collections.emptyList();
        }
        List<HomepageCourse> courses = homepageCourseDao.findAllById(request.getIds());
        return courses.stream()
                .map(this::buildCourseInfo)
                .collect(Collectors.toList());
    }

    /**
     * 根据数据记录构造对象信息
     *
     * @param course 课程记录
     * @return 对象信息
     */
    private CourseInfo buildCourseInfo(HomepageCourse course) {
        return CourseInfo.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseType(course.getCourseType() == 0 ? "免费课程" : "实战课程")
                .courseIcon(course.getCourseIcon())
                .courseIntro(course.getCourseIntro())
                .build();
    }
}
