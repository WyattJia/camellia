package com.netease.nim.camellia.redis.proxy.uds;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

public class UnixServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object object) throws Exception {
        System.out.println("Received message from " + ctx.channel().remoteAddress() + ": " + object);
        ctx.writeAndFlush("Hello from Unix server!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Exception caught on " + ctx.channel().remoteAddress() + "Error occurred: " + cause.getMessage() );
        cause.printStackTrace();
        ctx.close();
    }
}
