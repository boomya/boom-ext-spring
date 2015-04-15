package com.boom.ext.spring.thread;

/**
 * Created by jiangshan on 15/3/25.
 */
public class Task {
    //任务名称
    private String taskName = null;
    //任务启始时间, 如果为0则是马上开始
    private long startTime = 0L;
    //任务结束时间, 如果为0则是一直执行
    private long endTime = 0L;
    //任务执行的间隔时间, 默认为1小时
    private long intervalTime = 1000 * 60 * 60;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }
}
