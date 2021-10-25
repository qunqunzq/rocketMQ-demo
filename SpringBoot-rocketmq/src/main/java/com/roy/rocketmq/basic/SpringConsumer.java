package com.roy.rocketmq.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roy.rocketmq.domain.User;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 注意下@RocketMQMessageListener这个注解的其他属性
 *
 * @author ：楼兰
 * @date ：Created in 2020/10/22
 * @description:
 **/
@Component
@RocketMQMessageListener(consumerGroup = "MyConsumerGroup", selectorExpression = "*",topic = "qun_sprinboot_test",messageModel = MessageModel.CLUSTERING ,consumeMode = ConsumeMode.ORDERLY)
public class SpringConsumer implements RocketMQListener<User> {

    @Override
    public void onMessage(User message) {
        System.out.println("接收到到消息：" + message.getUserAge());
    }

}
