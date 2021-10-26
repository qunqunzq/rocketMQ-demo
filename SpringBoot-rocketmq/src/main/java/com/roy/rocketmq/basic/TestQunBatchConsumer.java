package com.roy.rocketmq.basic;

import com.roy.rocketmq.domain.User;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/26 11:24 上午
 */
@Component
@RocketMQMessageListener(consumerGroup = "TestQunBatch", selectorExpression = "batch", topic = "master", messageModel = MessageModel.CLUSTERING, consumeMode = ConsumeMode.ORDERLY)
public class TestQunBatchConsumer  implements RocketMQListener<User>{
    @Override
    public void onMessage(User message) {
        System.out.println("消费"+message);
    }
}