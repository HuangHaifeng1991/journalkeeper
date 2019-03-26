package com.jd.journalkeeper.rpc.transport.codec;

import com.jd.journalkeeper.rpc.transport.command.Header;
import io.netty.buffer.ByteBuf;

/**
 * jmq消息体解码
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/8/21
 */
public interface PayloadDecoder<H extends Header> {

    public Object decode(H header, ByteBuf buffer) throws Exception;
}