package com.fleet.schedule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * 基于接口
 */
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                () -> System.out.println("任务执行时间" + System.currentTimeMillis()),
                triggerContext -> new CronTrigger("*/5 * * * * ?").nextExecutionTime(triggerContext)
        );
    }
}
