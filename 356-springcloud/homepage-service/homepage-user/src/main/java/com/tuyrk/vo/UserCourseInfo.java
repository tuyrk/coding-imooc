package com.tuyrk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 用户课程对象
 *
 * @author tuyrk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseInfo {
    private UserInfo userInfo;
    private List<CourseInfo> courseInfos;

    public static UserCourseInfo invalid() {
        return new UserCourseInfo(new UserInfo(), Collections.emptyList());
    }
}
