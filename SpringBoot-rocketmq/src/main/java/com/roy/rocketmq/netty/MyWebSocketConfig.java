package com.roy.rocketmq.netty;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/11/16 2:36 下午
 */
public class MyWebSocketConfig {
    public static Map<String, ChannelGroup> mapChannel = new ConcurrentHashMap<>();
}