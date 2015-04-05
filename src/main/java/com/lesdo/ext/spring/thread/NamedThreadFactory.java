package com.lesdo.ext.spring.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jiangshan on 15/3/24.
 */
public class NamedThreadFactory implements ThreadFactory {
    private String name = null;
    public NamedThreadFactory(String name){
        this.name = name;
    }

    private AtomicInteger tag = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(name + "-ThreadPool-thread：" + tag.getAndIncrement());
        return thread;
    }
}
