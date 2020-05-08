package com.fleet.websocket.controller;

import com.fleet.websocket.entity.Msg;
import com.fleet.websocket.entity.User;
import com.fleet.websocket.service.MsgQueue;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(path = "/msg")
    String msg() {
        return "msg";
    }

    @GetMapping(path = "/msg2")
    String msg2() {
        return "msg2";
    }

    @GetMapping(path = "/msg3")
    String msg3() {
        return "msg3";
    }

    /**
     * 广播推送消息
     */
    @Scheduled(fixedRate = 10000)
    public void sendTopicMessage() {
        User user = new User();
        user.setId(1L);
        Msg msg = new Msg(new Date().getTime(), "广播推送消息");
        simpMessagingTemplate.convertAndSend("/topic/getResponse", msg);
    }

    /**
     * 一对一推送消息
     */
    @Scheduled(fixedRate = 10000)
    public void sendQueueMessage() {
        User user = new User();
        user.setId(1L);
        Msg msg = new Msg(new Date().getTime(), "一对一推送消息");
        simpMessagingTemplate.convertAndSendToUser(user.getId() + "", "/queue/getResponse", msg);
    }

    /**
     * 定时产生消息
     */
    @Scheduled(fixedRate = 1000)
    public void outputLogger() {
        Msg msg = new Msg(new Date().getTime(), "这是测试");
        MsgQueue.getInstance().push(msg);
    }

    /**
     * 推送信息到 /topic/msg
     */
    @PostConstruct
    public void pushMsg() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Msg msg = MsgQueue.getInstance().poll();
                        if (msg != null) {
                            if (simpMessagingTemplate != null)
                                simpMessagingTemplate.convertAndSend("/topic/msg", msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        executorService.submit(runnable);
    }
}
