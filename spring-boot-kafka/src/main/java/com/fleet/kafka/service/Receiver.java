package com.fleet.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Receiver {

    // 接收消息
    @KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> optional = Optional.ofNullable(record.value());
        if (optional.isPresent()) {
            Object message = optional.get();
            System.out.println(record);
            System.out.println(message);
        }
    }
}
