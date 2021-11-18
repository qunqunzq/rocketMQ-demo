package com.roy.rocketmq.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/11/5 1:50 下午
 */
@Component
@ChannelHandler.Sharable
public class NettyServerHandler  extends SimpleChannelInboundHandler<String> {

    public static ConcurrentHashMap<String, Channel> batchIdChannel = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        final Channel channel = ctx.channel();
        if (msg.equals("111")){
            channel.writeAndFlush("success");
        }else {
            batchIdChannel.put(msg,channel);
            channel.writeAndFlush("wait");
        }
        System.out.println("消息"+ msg);
        System.out.println("batchIdChannel size="+batchIdChannel.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        String key = null;
        for (Map.Entry<String, Channel> entry : batchIdChannel.entrySet()){
            if (entry.getValue() == channel){
                key = entry.getKey();
            }
        }
        if (null != key ){
            batchIdChannel.remove(key);
        }
    }
}