package com.roy.rocketmq;

import com.alibaba.fastjson.TypeReference;
import com.roy.rocketmq.domain.OrderPaidEvent;
import com.roy.rocketmq.domain.ProductWithPayload;
import com.roy.rocketmq.domain.User;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2020/11/28
 * @description:
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringRocketTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void sendMessageTest(){
        String springTopic="TestTopic";
        //发送字符消息
        SendResult sendResult = rocketMQTemplate.syncSend(springTopic, "Hello, World!");
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", springTopic, sendResult);

    }


}
