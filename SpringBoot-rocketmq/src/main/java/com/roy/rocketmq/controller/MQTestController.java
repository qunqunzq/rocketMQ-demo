package com.roy.rocketmq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roy.rocketmq.basic.SpringProducer;
import com.roy.rocketmq.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ：楼兰
 * @date ：Created in 2020/10/27
 * @description:
 **/
@RestController
@RequestMapping("/MQTest")
public class MQTestController {

    private final String topic = "qun_sprinboot_test";
    @Resource
    private SpringProducer producer;
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestBody User message){
        producer.sendMessage(topic, JSONObject.toJSONString(message));
        return "消息发送完成";
    }

    //这个发送事务消息的例子中有很多问题，需要注意下。
    @RequestMapping("/sendTransactionMessage")
    public String sendTransactionMessage(String message) throws InterruptedException {
        producer.sendMessageInTransaction(topic,message);
        return "消息发送完成";
    }


    @RequestMapping("/syncSend")
    public String syncSend(@RequestBody User message) {
        producer.syncSend(JSON.toJSONString(message),"qun_test","sync");
        return "消息发送完成";
    }

    @RequestMapping("/asyncSend")
    public String asyncSend(@RequestBody User message) {
        producer.asyncSend(JSON.toJSONString(message),"qun_test","sync");
        return "消息发送完成";
    }

    @RequestMapping("/syncSendOrderly")
    public String syncSendOrderly(@RequestBody User message) {
        producer.syncSendOrderly(JSON.toJSONString(message),"master","order","10086");
        return "消息发送完成";
    }

}