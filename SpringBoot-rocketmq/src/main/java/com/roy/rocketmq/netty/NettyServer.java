package com.roy.rocketmq.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/11/5 1:48 下午
 */
@Component
public class NettyServer {

    @Autowired
    private NettyServerHandler nettyServerHandler;


    @PostConstruct
    public void run() throws Exception {
        CompletableFuture.runAsync(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workGroup = new NioEventLoopGroup(2);
            ServerBootstrap bootstrap = new ServerBootstrap();
            try {
                bootstrap.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                //处理http get请求
                                ch.pipeline().addLast("http-codec",new HttpServerCodec());
                                //处理http post请求
                                ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
                                //大数据流的支持
                                ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                                ch.pipeline().addLast(new MyWebSocketHandler());
                            }
                        });
                System.out.println("聊天室启动啦～～");
                final ChannelFuture sync = bootstrap.bind(8888).sync();
                sync.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        });

    }
}