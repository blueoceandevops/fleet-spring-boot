package com.fleet.artemis.controller;

import com.fleet.artemis.service.Sender;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.Destination;

@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private Sender sender;

    @RequestMapping(path = "/send")
    public void send(String msg) {
        System.out.println("发送消息：" + msg);
        // Destination destination = new ActiveMQQueue("test.queue");
        Destination destination = new ActiveMQQueue("test.queue");
        sender.send(destination, msg);
    }

//    @JmsListener(destination = "test.queue")
//    @SendTo("otest.queue")
//    public void receive1(String msg) {
//        System.out.println("接收到消息1：" + msg);
//    }
//
//    @JmsListener(destination = "otest.queue")
//    public void receive2(String msg) {
//        System.out.println("接收到消息2：" + msg);
//    }
}
