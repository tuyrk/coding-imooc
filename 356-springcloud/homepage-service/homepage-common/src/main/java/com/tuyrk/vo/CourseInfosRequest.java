package com.tuyrk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 课程信息请求对象
 *
 * @author tuyrk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfosRequest {
    private List<Long> ids;
}
