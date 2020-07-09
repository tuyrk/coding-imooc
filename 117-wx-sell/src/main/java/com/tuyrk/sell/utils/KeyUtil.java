package com.tuyrk.sell.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/10 9:32 星期四
 * Description:
 */
public class KeyUtil {
    /**
     * 生成唯一的主键
     *
     * @return 格式：时间 + 随机数
     */
    public static synchronized String getUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(currentTime.getTime()) + String.valueOf(number);
    }
}
