package com.tuyrk.sell.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/12 14:58 星期六
 * Description:
 * Json格式与对象格式互转
 */
public class JsonUtil {
    public static String toJson(Object obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.disableHtmlEscaping();
        Gson gson = gsonBuilder.create();
        return gson.toJson(obj);
    }

    public static Object toObject(String json, Object obj) {
        Gson gson = new Gson();
        return gson.fromJson(json, obj.getClass());
    }
}
