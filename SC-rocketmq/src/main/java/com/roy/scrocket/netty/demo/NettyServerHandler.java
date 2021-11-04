package com.roy.scrocket.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/27 4:34 下午
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        final Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[用户]" + channel.remoteAddress() + "上线了" + LocalDateTime.now());
        channelGroup.add(channel);
        System.out.println(channel.remoteAddress()+"上线了" + "\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[用户]" +channel.remoteAddress() + "下线了\n");
        System.out.println(channel.remoteAddress() + "下线了\n");
        System.out.println("channelGroup size="+channelGroup.size());
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        final Channel channel = ctx.channel();
        channelGroup.forEach(c ->{
            if (channel !=c ){
                c.writeAndFlush("[用户]" + c.remoteAddress() + "发送了消息：" + msg + "\n" );
            }else {
                c.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
}