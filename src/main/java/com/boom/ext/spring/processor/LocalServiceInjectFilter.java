package com.boom.ext.spring.processor;

import com.boom.ext.spring.ServiceLocator;
import org.jessma.mvc.Action;
import org.jessma.mvc.ActionExecutor;
import org.jessma.mvc.ActionFilter;

/**
 * Created by jiangshan on 15/4/3.
 */
public class LocalServiceInjectFilter implements ActionFilter {

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String doFilter(ActionExecutor executor) throws Exception {
        Action action = executor.getAction();

        ServiceLocator.setField(action);

        return executor.invoke();
    }
}
