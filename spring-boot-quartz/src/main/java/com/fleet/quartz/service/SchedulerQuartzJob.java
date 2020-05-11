package com.fleet.quartz.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class SchedulerQuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext arg0) {
        System.out.println("任务执行时间" + System.currentTimeMillis());
    }
}
