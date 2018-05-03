package com.xiaomo.travelhelper.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 响应编码器(MoResponse -> byte)
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——----——+——----——+
 * |  包头	| 模块号  | 命令号  | 结果码 |  长度   | 数据   |
 * +——----——+——-----——+——----——+——----——+——----——+——----——+
 * 包头4字节
 * 模块号2字节
 * 命令号2字节
 * 结果码2字节
 * 数据长度4字节
 */
public class MoResponseEncoder extends MessageToByteEncoder<MoResponse>{

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MoResponse moResponse, ByteBuf byteBuf) throws Exception {

        System.out.println("MoResponseEncoder 编码开始");

        byteBuf.writeInt(CodecConst.PROTOCOL_FLAG);
        byteBuf.writeShort(moResponse.getModule());
        byteBuf.writeShort(moResponse.getCmd());
        byteBuf.writeShort(moResponse.getStatusCode());
        byteBuf.writeInt(moResponse.getLength());
        if(moResponse.getLength() > 0){
            byteBuf.writeBytes(moResponse.getData());
        }

    }


}
