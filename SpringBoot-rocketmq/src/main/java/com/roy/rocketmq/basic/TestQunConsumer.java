package com.roy.rocketmq.basic;

import com.roy.rocketmq.domain.User;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/25 3:23 下午
 */
@Component
@RocketMQMessageListener(consumerGroup = "TestQun", selectorExpression = "order", topic = "master", messageModel = MessageModel.CLUSTERING, consumeMode = ConsumeMode.ORDERLY)
public class TestQunConsumer implements RocketMQListener<User> , RocketMQPushConsumerLifecycleListener {
    @Override
    public void onMessage(User message) {
        System.out.println(message);
        throw new RuntimeException();
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // 设置最大重试次数
        consumer.setMaxReconsumeTimes(5);
        // 如下，设置其它consumer相关属性
        consumer.setPullBatchSize(16);
    }
}