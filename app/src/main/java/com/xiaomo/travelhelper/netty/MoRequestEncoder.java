package com.xiaomo.travelhelper.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 请求编码器(MoRequest -> byte)
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号  | 命令号 | 长度   |  数据    |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * 包头4字节
 * 模块号2字节
 * 命令号2字节
 * 数据长度4字节
 */
public class MoRequestEncoder extends MessageToByteEncoder<MoRequest>{

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MoRequest moRequest, ByteBuf byteBuf) throws Exception {

        System.out.println("MoRequestEncoder 编码开始");

        byteBuf.writeInt(CodecConst.PROTOCOL_FLAG);
        byteBuf.writeShort(moRequest.getModule());
        byteBuf.writeShort(moRequest.getCmd());
        byteBuf.writeInt(moRequest.getLength());
        if(moRequest.getLength() > 0){
            byteBuf.writeBytes(moRequest.getData());
        }
    }

}
