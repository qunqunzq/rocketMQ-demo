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
 * @date 2021/10/25 4:14 下午
 */
@Component
@RocketMQMessageListener(consumerGroup = "TestQunDLQ", selectorExpression = "order", topic = "%DLQ%TestQun", messageModel = MessageModel.CLUSTERING, consumeMode = ConsumeMode.ORDERLY)
public class TestQunDLQConsumer implements RocketMQListener<User>, RocketMQPushConsumerLifecycleListener {
    @Override
    public void onMessage(User message) {
        System.out.println("22222"+message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {

    }
}