package com.fleet.kafka.service;

import com.alibaba.fastjson.JSON;
import com.fleet.kafka.entity.Msg;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class Sender {

    @Resource
    private KafkaTemplate kafkaTemplate;

    // 发送消息
    public void send(String msg) {
        Msg message = new Msg();
        message.setId("KFK_" + System.currentTimeMillis());
        message.setMsg(msg);
        message.setSendTime(new Date());
        kafkaTemplate.send("test", JSON.toJSONString(message));
    }
}
