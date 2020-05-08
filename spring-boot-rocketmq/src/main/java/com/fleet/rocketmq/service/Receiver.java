package com.fleet.rocketmq.service;

import com.fleet.rocketmq.config.RocketMQConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class Receiver {

    @Resource
    private RocketMQConfig rocketMQConfig;

    /**
     * 初始化 RocketMQ 的监听信息，渠道信息
     */
    public void receive(String topic, String tags) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMQConfig.getGroup());
        consumer.setNamesrvAddr(rocketMQConfig.getNamesrvAddr());

        // 订阅 topic 下 subExpression 的消息,都订阅消息
        consumer.subscribe(topic, tags);
        // 程序第一次启动从消息队列头获取数据
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 可以修改每次消费消息的数量，默认设置是每次消费一条
        consumer.setConsumeMessageBatchMaxSize(1);
        // 开启内部类实现监听
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
                // 会把不同的消息分别放置到不同的队列中
                for (MessageExt message : messages) {
                    System.out.println("接收到了消息：" + new String(message.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
