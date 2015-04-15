package com.boom.ext.spring.processor;

import com.boom.ext.spring.ServiceLocator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by jiangshan on 15/4/3.
 */
public class LocalServiceBeanInit implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceLocator.init(applicationContext);
    }
}
