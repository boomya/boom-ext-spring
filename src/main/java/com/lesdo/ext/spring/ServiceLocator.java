package com.lesdo.ext.spring;

import com.lesdo.ext.spring.annotation.LocalService;
import com.lesdo.ext.spring.annotation.LocalServiceField;
import org.jessma.util.BeanHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        /** 装配所有带自定义注解的bean **/
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(LocalService.class);
        for (Object serviceBean : serviceBeanMap.values()) {
            /** 查找init方法并执行 **/
            try{
                Method method = serviceBean.getClass().getMethod("init");
                if(method != null){
                    method.setAccessible(true);
                    method.invoke(serviceBean);
                }
            } catch (NoSuchMethodException e) {
                System.err.println("no [init] method. " + e.getMessage());
            } catch (InvocationTargetException e) {
                System.err.println(e.getMessage());
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
            /** 加载bean到map中, 便于后续通过map直接获取bean **/
            Class<?>[] classes = serviceBean.getClass().getInterfaces();
            if(classes==null || classes.length<=0){
                continue;
            }
            String serviceName = classes[0].getSimpleName();
            serviceMap.put(serviceName, serviceBean);
        }

        /** 自动注入所有被装配bean中带有自定义注解的field **/
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
