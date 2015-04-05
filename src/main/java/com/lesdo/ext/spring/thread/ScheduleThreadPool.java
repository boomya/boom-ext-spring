package com.lesdo.ext.spring.thread;

import org.jessma.util.LogUtil;
import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangshan on 15/3/25.
 */
public class ScheduleThreadPool {

    private static final Logger                   logger                    = LogUtil.getLogger("lesdo");
    private static final int                      CORE_THREAD_SIZE          = 20;
    private final static ScheduledExecutorService scheduledExecutorServices = Executors.newScheduledThreadPool(
            CORE_THREAD_SIZE, new NamedThreadFactory("schedule"));

    public static ScheduledFuture<?> execute(Runnable command, Task task) {
//        return scheduledExecutorServices.scheduleWithFixedDelay(command, 0L, 1000L, TimeUnit.MILLISECONDS);

        long initDelay = 0L;
        if (task.getStartTime() > 0L) {
            initDelay = System.currentTimeMillis() - task.getStartTime();
        }
        long delay = task.getIntervalTime();

        logger.info("registe task(" + task.getTaskName() + "), after " + initDelay/1000 + "(s) start, with " + delay/1000 + "(s) again.");

        return scheduledExecutorServices.scheduleWithFixedDelay(command, initDelay, delay, TimeUnit.MILLISECONDS);
    }

}

