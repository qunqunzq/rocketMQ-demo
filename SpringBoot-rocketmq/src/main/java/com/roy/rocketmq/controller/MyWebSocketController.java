package com.roy.rocketmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.roy.rocketmq.domain.User;
import com.roy.rocketmq.netty.MyWebSocketConfig;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/11/16 2:39 下午
 */
@RestController
@RequestMapping("/mySocket")
public class MyWebSocketController {

    @GetMapping("/sendMessage")
    public String sendMessage(@RequestParam String message){
        final ChannelGroup channels = MyWebSocketConfig.mapChannel.get(message);
        if (channels != null){
            final TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("成功啦！");
            channels.writeAndFlush(textWebSocketFrame);
        }
        return "消息发送完成";
    }
}