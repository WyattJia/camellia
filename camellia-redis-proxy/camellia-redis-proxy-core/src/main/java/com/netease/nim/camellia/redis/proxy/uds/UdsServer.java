package com.netease.nim.camellia.redis.proxy.uds;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.SocketAddress;

public class UdsServer {
    public static void main(String[] args) throws Exception{
        SocketAddress address = new DomainSocketAddress("/tmp/camellia.sock");

        EventLoopGroup group = new EpollEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(EpollServerDomainSocketChannel.class)
                    .localAddress(address)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new HttpRequestDecoder());
                            channel.pipeline().addLast(new HttpObjectAggregator(65536));
                            channel.pipeline().addLast(new HttpResponseEncoder());
                            channel.pipeline().addLast(new UnixServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("Server started at " + address);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
