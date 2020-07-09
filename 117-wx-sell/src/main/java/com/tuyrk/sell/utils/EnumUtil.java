package com.tuyrk.sell.utils;

import com.tuyrk.sell.enums.CodeEnum;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/14 16:14 星期一
 * Description:
 * 根据枚举类型的code，获得枚举类型的实例
 */
public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T e : enumClass.getEnumConstants()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;
    }
}
