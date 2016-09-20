package com.pengpeng.stargame.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * JSON 工具类，使用 gson 作为 json 的实现.
 *
 * @author ChenHonghong@gmail.com
 * @since 3.7
 */
public final class JsonUtil {
// ------------------------------ 属性 ------------------------------

// -------------------------- 静态方法 --------------------------

    public static String convertMap(Map object) throws Exception {
        return convertMap(object, true);
    }

    public static String convertMap(Map object, boolean format) throws Exception {
        Iterator ite = object.keySet().iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            if (object.get(key) == null) {
                object.put(key, "");
            }
        }
        return toJson(object);
    }

    public static String toJson(Object object) throws Exception {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(object);
    }

    public static Map<String, String> fromJsonToMap(String json) throws Exception {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * 将 json 字符串转换成对象.
     * @param json 字符串
     * @param clazz 对象类对象
     * @return 对象
     * @Exception 如果转换过程中出错，那么抛出异常，调用者必须捕捉处理
     */
    public static Object fromJson(String json, Class clazz) throws Exception{
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(json,clazz);
    }

    public static String convert(Object object, boolean format) throws Exception {
        return toJson(object);
    }

    public static String convert(Collection object, boolean format) throws Exception {
        return toJson(object);
    }

    public static String convert(Boolean object, boolean format) throws Exception {
        return toJson(object);
    }
}
