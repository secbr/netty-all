package com.choupangxia.netty.server;

import com.choupangxia.netty.coder.StringDecoder;
import com.choupangxia.netty.coder.StringEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器端
 *
 * 来源：公众号，程序新视界
 * @author sec
 * @version 1.0
 * @date 2020/12/22
 **/
public class Server {

    public static void main(String[] args) throws Exception {
        int port = 9998;
        new Server().bind(port);
    }

    public void bind(int port) throws Exception {
        // 服务器线程组 用于网络事件的处理 一个用于服务器接收客户端的连接
        // 另一个线程组用于处理SocketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // NIO服务器端的辅助启动类 降低服务器开发难度
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    // 类似NIO中serverSocketChannel
                    .channel(NioServerSocketChannel.class)
                    // 配置TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 设置tcp缓冲区
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 设置发送缓冲大小
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    // 这是接收缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    // 保持连接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 网络事件处理器
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            // 增加自定义的编码器和解码器
                            channel.pipeline()
                                    .addLast(new StringEncoder())
                                    .addLast(new StringDecoder())
                                    // 服务端的处理器
                                    .addLast(new ServerHandler());
                        }
                    });

            // 服务器启动后 绑定监听端口 同步等待成功 主要用于异步操作的通知回调 回调处理用的ChildChannelHandler
            ChannelFuture f = serverBootstrap.bind(port).sync();
            System.out.println("Server启动");
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("服务器优雅的释放了线程资源…");
        }
    }
}
