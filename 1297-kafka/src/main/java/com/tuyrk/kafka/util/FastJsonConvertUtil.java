package com.tuyrk.kafka.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tuyrk
 */
@Slf4j
public class FastJsonConvertUtil {
    private static final SerializerFeature[] featuresWithNullValue = {
            SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse};

    /**
     * 将JSON字符串转换为实体对象
     *
     * @param data  JSON字符串
     * @param clzss 转换对象
     * @param <T>   转换对象的类型
     * @return T
     */
    public static <T> T convertJSONToObject(String data, Class<T> clzss) {
        try {
            return JSON.parseObject(data, clzss);
        } catch (Exception e) {
            log.error("convertJSONToObject Exception", e);
            return null;
        }
    }

    /**
     * 将JSONObject对象转换为实体对象
     *
     * @param data  JSON字符串
     * @param clzss 转换对象
     * @param <T>   转换对象的类型
     * @return T
     */
    public static <T> T convertJSONToObject(JSONObject data, Class<T> clzss) {
        try {
            return JSONObject.toJavaObject(data, clzss);
        } catch (Exception e) {
            log.error("convertJSONToObject Exception", e);
            return null;
        }
    }

    /**
     * 将JSON字符串数组转换为List集合对象
     *
     * @param data  JSON字符串
     * @param clzss 转换对象
     * @param <T>   转换对象的类型
     * @return T
     */
    public static <T> List<T> convertJSONToArray(String data, Class<T> clzss) {
        try {
            return JSON.parseArray(data, clzss);
        } catch (Exception e) {
            log.error("convertJSONToArray Exception", e);
            return null;
        }
    }

    /**
     * 将List<JSONObject>转换为List集合对象
     *
     * @param data  JSON字符串
     * @param clzss 转换对象
     * @param <T>   转换对象的类型
     * @return T
     */
    public static <T> List<T> convertJSONToArray(List<JSONObject> data, Class<T> clzss) {
        try {
            ArrayList<T> ts = new ArrayList<>();
            for (JSONObject jsonObject : data) {
                ts.add(convertJSONToObject(jsonObject, clzss));
            }
            return ts;
        } catch (Exception e) {
            log.error("convertJSONToArray Exception", e);
            return null;
        }
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String convertObjectToJSON(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("convertObjectToJSON Exception", e);
            return null;
        }
    }

    /**
     * 将对象转换为JSONObject对象
     *
     * @param obj 对象
     * @return JSONObject对象
     */
    public static JSONObject convertObjectToJSONObject(Object obj) {
        try {
            return (JSONObject) JSONObject.toJSON(obj);
        } catch (Exception e) {
            log.error("convertObjectToJSONObject Exception", e);
            return null;
        }
    }

    /**
     * 将对象转换为字符串，空即为默认值
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String convertObjectToJSONWithNullValue(Object obj) {
        try {
            return JSON.toJSONString(obj, featuresWithNullValue);
        } catch (Exception e) {
            log.error("convertObjectToJSONWithNullValue Exception", e);
            return null;
        }
    }
}
