package com.fleet.kafka.controller;

import com.fleet.kafka.service.Sender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private Sender sender;

    @RequestMapping("/send")
    public void send(String msg) {
        sender.send(msg);
    }
}
