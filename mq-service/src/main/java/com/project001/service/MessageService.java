package com.project001.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
//消息名称
@RocketMQMessageListener(consumerGroup = "message",topic = "myTop")
public class MessageService implements RocketMQListener<String> {
    @Resource
    private Websocket websocket;

    @Override
    public void onMessage(String message) {
        log.info("收到消息:{}", message);
        //调用sendMessage方法
        this.websocket.sendMessage(message);
    }
}
