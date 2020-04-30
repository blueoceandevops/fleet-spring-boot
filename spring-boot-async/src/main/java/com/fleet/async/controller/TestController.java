package com.fleet.async.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private AsyncTask asyncTask;

    @RequestMapping("start")
    public void start() throws Exception {
        System.out.println("任务开始");
        asyncTask.doAsync();
        System.out.println("任务结束");
    }
}
