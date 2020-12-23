package com.choupangxia.netty.client;

import com.choupangxia.netty.coder.StringDecoder;
import com.choupangxia.netty.coder.StringEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 *
 * 来源：公众号，程序新视界
 * @author sec
 * @version 1.0
 * @date 2020/12/22
 **/
public class Client {

    public static void main(String[] args) throws Exception {
        new Client().connect(9998, "127.0.0.1");
    }

    /**
     * 连接服务器
     */
    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 客户端辅助启动类 对客户端配置
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        // 网络事件处理器
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            // 增加自定义的编码器和解码器
                            channel.pipeline()
                                    .addLast(new StringEncoder())
                                    .addLast(new StringDecoder())
                                    // 客户端的处理器
                                    .addLast(new ClientHandler());
                        }
                    });
            // 异步链接服务器 同步等待链接成功
            ChannelFuture f = b.connect(host, port).sync();
            System.out.println(f);
            // 发送消息
            Thread.sleep(1000);
            f.channel().writeAndFlush("Hello ");
            f.channel().writeAndFlush("World ");
            Thread.sleep(2000);
            f.channel().writeAndFlush("Netty ");
            // 等待链接关闭
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
            System.out.println("客户端优雅的释放了线程资源...");
        }
    }
}
