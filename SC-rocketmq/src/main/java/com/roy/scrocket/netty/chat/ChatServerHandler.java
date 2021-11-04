package com.roy.scrocket.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/28 10:19 上午
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channels =  new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channels.writeAndFlush("[客户端]"+channel.remoteAddress()+"上线了"+ LocalDateTime.now());
        channels.add(channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        final Channel channel = ctx.channel();
        channels.forEach(u->{
            if (u != channel) {
                u.writeAndFlush("[客户端]"+channel.remoteAddress() + "发送了消息"+msg +"\n");
            }else {
                u.writeAndFlush("[自己]发送了消息"+ msg +"\n");
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channels.writeAndFlush("[客户端]"+channel.remoteAddress()+"下线了"+"\n");
        System.out.println("channelgrop 集合大小" + channels.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}