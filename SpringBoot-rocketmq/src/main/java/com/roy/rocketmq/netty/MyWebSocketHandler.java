package com.roy.rocketmq.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/11/5 6:08 下午
 */

public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;
    private static final String web_socket_url = "ws://qun:8888/websocket";


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            final String type = (String) ctx.attr(AttributeKey.valueOf("type")).get();
            if ("ssss".equals(type)){
                System.out.println("收到ssss");
            }else if ("socket".equals(type)){
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //验证消息是close
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        }
        //验证ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PingWebSocketFrame(frame.content().retain()));
            return;
        }
        //二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new RuntimeException("不支持二进制消息");
        }
        final String text = ((TextWebSocketFrame) frame).text();
        ChannelGroup channels = MyWebSocketConfig.mapChannel.get(text);
        if (null == channels){
            synchronized (text){
                if (null == channels){
                    channels =  new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                    channels.add(ctx.channel());
                    MyWebSocketConfig.mapChannel.put(text,channels);
                }else {
                    channels.add(ctx.channel());
                }
            }
        }else {
            channels.add(ctx.channel());
        }
    }


    public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.getDecoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))) {
            sendHttpRequest(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        final HttpMethod method = request.getMethod();
        final String uri = request.getUri();
        if (method == HttpMethod.GET && "/webssss".equals(uri)){
            ctx.attr(AttributeKey.valueOf("type")).set("ssss");
        }else if (method == HttpMethod.GET && "/websocket".equals(uri)){
            ctx.attr(AttributeKey.valueOf("type")).set("socket");
        }
        final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(web_socket_url, null, false);
        handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 通过它构造握手响应消息返回给客户端，
            // 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码，
            // 添加WebSocketEncoder和WebSocketDecoder之后，服务端就可以自动对WebSocket消息进行编解码了
            handshaker.handshake(ctx.channel(), request);
        }
    }

    private void sendHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request, DefaultFullHttpResponse response) {
        if (response.getStatus().code() != 200) {
            final ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        final ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (response.getStatus().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        MyWebSocketConfig.mapChannel.values().forEach(u->u.remove(ctx.channel()));
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        MyWebSocketConfig.mapChannel.values().forEach(u->u.remove(ctx.channel()));
    }

}