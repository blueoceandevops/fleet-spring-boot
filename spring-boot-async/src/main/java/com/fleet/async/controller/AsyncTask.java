package com.fleet.async.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

    @Async
    public void doAsync() throws InterruptedException {
        System.out.println("执行任务开始");
        Thread.sleep(1000 * 10);
        System.out.println("执行任务结束");
    }
}
