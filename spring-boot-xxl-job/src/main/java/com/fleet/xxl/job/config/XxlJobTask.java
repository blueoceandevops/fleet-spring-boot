package com.fleet.xxl.job.config;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlJobTask {

    @XxlJob("jobHandler")
    public ReturnT<String> execute(String param) throws InterruptedException {
        System.out.println("任务执行时间" + System.currentTimeMillis());
        return ReturnT.SUCCESS;
    }
}
