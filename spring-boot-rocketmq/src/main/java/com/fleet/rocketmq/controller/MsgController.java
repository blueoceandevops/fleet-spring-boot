package com.fleet.rocketmq.controller;

import com.fleet.rocketmq.service.Receiver;
import com.fleet.rocketmq.service.Sender;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private Sender sender;

    @Resource
    private Receiver receiver;

    @RequestMapping("/send")
    public void send(String msg) throws InterruptedException, RemotingException, UnsupportedEncodingException, MQClientException, MQBrokerException {
        sender.send("PushTopic", "push", msg);
    }

    @RequestMapping("/receiver")
    public void receiver() throws MQClientException {
        receiver.receive("PushTopic", "push");
    }
}
