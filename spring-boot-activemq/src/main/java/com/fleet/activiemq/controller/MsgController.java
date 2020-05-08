package com.fleet.activiemq.controller;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.Destination;

@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @RequestMapping(path = "/send")
    public void send(String msg) {
        System.out.println("发送消息" + msg);
        // Destination destination = new ActiveMQQueue("test.queue");
        Destination destination = new ActiveMQTopic("test.queue");
        jmsMessagingTemplate.convertAndSend(destination, msg);
    }

    @JmsListener(destination = "test.queue", containerFactory = "customJmsListenerContainerFactory")
    public void receive(String msg) {
        System.out.println("接收到消息1：" + msg);
    }

    @JmsListener(destination = "test.queue", containerFactory = "customJmsListenerContainerFactory")
    public void receive2(String msg) {
        System.out.println("接收到消息2：" + msg);
    }
}
