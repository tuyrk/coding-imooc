package com.tuyrk.activiti7.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tuyrk@qq.com
 * @date 2021/4/5 14:24
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 错误
     */
    ERROR(1, "错误"),
    ;

    private final int code;
    private final String desc;
}
