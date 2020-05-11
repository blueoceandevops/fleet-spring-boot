package com.fleet.quartz.controller;

import com.fleet.quartz.service.SchedulerQuartzJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class CronJobController {

    private static final Logger logger = LoggerFactory.getLogger(CronJobController.class);

    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    @RequestMapping("/insert")
    public String insert() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey("job_name", "job_group");
            TriggerKey triggerKey = new TriggerKey("trigger_name", "trigger_group");
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");
            JobDetail jobDetail = JobBuilder
                    .newJob(SchedulerQuartzJob.class)
                    .withIdentity(jobKey)
                    .withDescription("定时执行任务")
                    .build();
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(jobKey)
                    .withSchedule(cronScheduleBuilder)
                    .startNow()
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            logger.error("创建定时任务失败", e);
            return "创建定时任务失败";
        }
        return "成功";
    }

    @RequestMapping("/delete")
    public String delete() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey("job_name", "job_group");
            TriggerKey triggerKey = new TriggerKey("trigger_name", "trigger_group");

            scheduler.pauseTrigger(triggerKey); // 停止触发器
            scheduler.unscheduleJob(triggerKey); // 移除触发器
            scheduler.deleteJob(jobKey); // 删除任务
        } catch (SchedulerException e) {
            logger.error("删除定时任务失败", e);
            return "删除定时任务失败";
        }

        return "成功";
    }

    @RequestMapping("/update")
    public String update() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey("job_name", "job_group");
            TriggerKey triggerKey = new TriggerKey("trigger_name", "trigger_group");
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("*/20 * * * * ?");
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey); // 停止触发器
                scheduler.unscheduleJob(triggerKey); // 移除触发器
            }
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                scheduler.deleteJob(jobKey); // 删除任务
            }
            JobDetail jobDetail = JobBuilder
                    .newJob(SchedulerQuartzJob.class)
                    .withIdentity(jobKey)
                    .withDescription("定时执行任务")
                    .build();
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(jobKey)
                    .withSchedule(cronScheduleBuilder)
                    .startNow()
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            logger.error("修改定时任务失败", e);
            return "修改定时任务失败";
        }
        return "成功";
    }

    @RequestMapping("/pause")
    public String pause() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey("job_name", "job_group");
            TriggerKey triggerKey = new TriggerKey("trigger_name", "trigger_group");
            scheduler.pauseJob(jobKey);
            scheduler.pauseTrigger(triggerKey);
        } catch (Exception e) {
            logger.error("暂停定时任务失败", e);
            return "暂停定时任务失败";
        }
        return "成功";
    }

    @RequestMapping("/resume")
    public String restart() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey("job_name", "job_group");
            TriggerKey triggerKey = new TriggerKey("trigger_name", "trigger_group");
            scheduler.resumeJob(jobKey);
            scheduler.resumeTrigger(triggerKey);
        } catch (Exception e) {
            logger.error("恢复定时任务失败", e);
            return "恢复定时任务失败";
        }
        return "成功";
    }

    @RequestMapping("/start")
    public void start() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/shutdown")
    public void shutdown() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
