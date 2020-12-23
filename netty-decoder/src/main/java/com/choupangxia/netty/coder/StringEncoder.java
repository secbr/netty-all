package com.choupangxia.netty.coder;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 自定义字符串编码器
 * 来源：公众号，程序新视界
 * @author sec
 * @version 1.0
 * @date 2020/12/22
 **/
public class StringEncoder extends MessageToMessageEncoder<CharSequence> {

    private final Charset charset;

    public StringEncoder() {
        this(Charset.defaultCharset());
    }

    public StringEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) {
        // 发送消息为空直接返回
        if (msg.length() == 0) {
            return;
        }
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg),
                this.charset));
    }
}
