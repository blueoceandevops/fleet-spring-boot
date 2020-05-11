//package com.fleet.schedule.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//
///**
// * 基于注解
// */
//@Configuration
//public class ScheduleTask {
//
//    // 表达式
//    @Scheduled(cron = "*/5 * * * * ?")
//    private void tesk1() {
//        System.out.println("任务1执行时间" + System.currentTimeMillis());
//    }
//
//    // 指定时间间隔，例如：5秒，fixedDelay 的间隔时间是根据上次的任务结束的时候开始计时的
//    @Scheduled(fixedDelay = 5000)
//    private void tesk2() {
//        System.out.println("任务2执行时间" + System.currentTimeMillis());
//    }
//
//    // 指定时间间隔，例如：5秒, fixedRate 的间隔时间是根据上次任务开始的时候计时的
//    @Scheduled(fixedRate = 5000)
//    private void tesk3() {
//        System.out.println("任务3执行时间" + System.currentTimeMillis());
//    }
//}
