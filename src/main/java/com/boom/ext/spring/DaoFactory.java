package com.boom.ext.spring;

import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangshan on 15/4/8.
 */
public class DaoFactory {

    private static Map<String, Object> daoMap = null;

    public static void init(ApplicationContext applicationContext) {
        if (daoMap != null) {
            return;
        }
        daoMap = new HashMap<String, Object>();
    }
}
