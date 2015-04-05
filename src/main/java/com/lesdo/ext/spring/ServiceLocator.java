package com.lesdo.ext.spring;

import com.lesdo.ext.spring.annotation.LocalService;
import com.lesdo.ext.spring.annotation.LocalServiceField;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangshan on 15/4/2.
 */
public class ServiceLocator {

    private static Map<String, Object> serviceMap = null;

    public static void init(ApplicationContext applicationContext) {

        if (serviceMap != null) {
            return;
        }
        serviceMap = new HashMap<String, Object>();

        //装配所有带自定义注解的bean
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(LocalService.class);
        for (Object serviceBean : serviceBeanMap.values()) {
            serviceMap.put(serviceBean.getClass().getAnnotation(LocalService.class).value().getSimpleName(),
                           serviceBean);
        }

        //自动注入所有被装配bean中带有自定义注解的field
        for (Object serviceBean : serviceMap.values()) {
            setField(serviceBean);
        }
    }

    public static void setField(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(LocalServiceField.class)) {
                Object value = serviceMap.get(field.getType().getSimpleName());
                if (value == null) {
                    continue;
                }
//                    BeanHelper.setFieldValue(serviceBean, field, value);
                field.setAccessible(true);
                try {
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static <T> T getService(Class<T> clazz) {
        if (serviceMap.containsKey(clazz.getSimpleName())) {
            return (T) serviceMap.get(clazz.getSimpleName());
        }
        return null;
    }
}
