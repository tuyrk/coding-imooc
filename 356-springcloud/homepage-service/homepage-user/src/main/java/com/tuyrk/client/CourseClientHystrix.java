package com.tuyrk.client;

import com.tuyrk.vo.CourseInfo;
import com.tuyrk.vo.CourseInfosRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 熔断降级策略
 *
 * @author tuyrk
 */
@Component
public class CourseClientHystrix implements CourseClient {
    @Override
    public CourseInfo getCourseInfo(Long id) {
        return CourseInfo.invalid();
    }

    @Override
    public List<CourseInfo> getCourseInfos(CourseInfosRequest request) {
        return Collections.emptyList();
    }
}
