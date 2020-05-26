package com.fleet.artemis.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @JmsListener(destination = "test.queue")
    public void receive(String msg) {
        System.out.println("接收到消息：" + msg);
    }
}
