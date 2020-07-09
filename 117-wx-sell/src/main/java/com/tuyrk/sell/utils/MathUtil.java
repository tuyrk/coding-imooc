package com.tuyrk.sell.utils;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/13 17:12 星期日
 * Description:
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /**
     * 比较两个金额是否相等
     */
    public static Boolean equals(Double d1, Double d2) {
        Double result = Math.abs(d1 - d2);
        return result < MONEY_RANGE;
    }
}
