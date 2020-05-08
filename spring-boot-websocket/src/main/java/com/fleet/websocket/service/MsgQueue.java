package com.fleet.websocket.service;

import com.fleet.websocket.entity.Msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MsgQueue {

    // 队列大小
    public static final int QUEUE_MAX_SIZE = 10000;

    private static MsgQueue alarmMessageQueue = new MsgQueue();

    // 阻塞队列
    private BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    private MsgQueue() {
    }

    public static MsgQueue getInstance() {
        return alarmMessageQueue;
    }

    /**
     * 消息入队
     */
    public boolean push(Msg msg) {
        return this.blockingQueue.add(msg);// 队列满了就抛出异常，不阻塞
    }

    /**
     * 消息出队
     */
    public Msg poll() {
        Msg result = null;
        try {
            result = (Msg) this.blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
