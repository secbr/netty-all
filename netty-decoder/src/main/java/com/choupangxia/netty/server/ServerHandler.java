package com.choupangxia.netty.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务器端处理器
 *
 * 来源：公众号，程序新视界
 * @author sec
 * @version 1.0
 * @date 2020/12/22
 **/
public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 接受客户端的数据
        String body = (String) msg;
        System.out.println(body.length());
        System.out.println("收到Client信息:[" + body +"]");
        // 服务端，回写数据给客户端，直接回写整形的数据
        String data = "Hello ,I am Server …";
        ctx.writeAndFlush(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
