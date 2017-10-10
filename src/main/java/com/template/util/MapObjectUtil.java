package com.template.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package com.template.util
 * @Description: 使用反射机制;map与object对象互相转换
 * @Author zhouxi
 * @Date 2017/9/29 23:24
 */
public class MapObjectUtil {

    /**
     * @Description: map转换成object
     * @param map
     * @param clazz
     * @return Object
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> clazz) {
        try{
            Object obj = clazz.newInstance();
            if(map != null && map.size() > 0) {
                for(Map.Entry<String, Object> entry : map.entrySet()) {
                    String propertyName = entry.getKey();   // 属性名
                    Object value = entry.getValue();        // value值
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: object转map
     * @param object
     * @return map
     */
    public static Map<String, Object> objectToMap(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();  // 获取声明的属性
        for(Field field : fields) {
            String keyName = field.getName();
            try {
                Object obj = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
