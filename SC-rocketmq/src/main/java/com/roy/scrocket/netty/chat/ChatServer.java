package com.roy.scrocket.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/28 10:13 上午
 */
public class ChatServer {
    public static void main(String[] args) {
        EventLoopGroup bossGrop = new NioEventLoopGroup(1);
        EventLoopGroup workGrop = new NioEventLoopGroup(2);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGrop,workGrop)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder()).addLast(new ChatServerHandler());
                        }
                    });
            final ChannelFuture sync = bootstrap.bind(9000).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGrop.shutdownGracefully();
            workGrop.shutdownGracefully();
        }
    }
}