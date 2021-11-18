package com.roy.rocketmq.netty;

import com.roy.rocketmq.domain.User;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/11/5 2:26 下午
 */
@Component
@RocketMQMessageListener(consumerGroup = "Consumer1", selectorExpression = "*",topic = "qun_test",messageModel = MessageModel.CLUSTERING ,consumeMode = ConsumeMode.CONCURRENTLY)
public class NettyMqConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {

    }
}