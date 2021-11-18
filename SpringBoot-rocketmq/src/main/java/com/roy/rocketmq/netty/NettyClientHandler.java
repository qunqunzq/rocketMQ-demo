package com.roy.rocketmq.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/27 5:29 下午
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("接收到服务器消息" + msg);
    }
}