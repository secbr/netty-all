package com.choupangxia.netty.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 客户端处理器
 *
 * 来源：公众号，程序新视界
 * @author sec
 * @version 1.0
 * @date 2020/12/22
 **/
public class ClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            String body = (String) msg;
            System.out.println("Client :" + body);

            // 只是读数据，没有写数据的话
            // 需要自己手动的释放的消息
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
