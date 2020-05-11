package com.fleet.schedule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 基于注解
 */
@Configuration
@EnableAsync
public class MultithreadScheduleTask {

    @Async
    @Scheduled(fixedDelay = 1000)  // 间隔1秒
    public void task1() throws InterruptedException {
        System.out.println("任务1执行时间" + System.currentTimeMillis());
        System.out.println("线程 : " + Thread.currentThread().getName());
        Thread.sleep(1000 * 10);
    }

    @Async
    @Scheduled(fixedDelay = 2000) // 间隔2秒
    public void task2() {
        System.out.println("任务2执行时间" + System.currentTimeMillis());
        System.out.println("线程 : " + Thread.currentThread().getName());
    }
}
