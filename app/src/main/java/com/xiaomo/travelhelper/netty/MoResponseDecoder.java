package com.xiaomo.travelhelper.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 响应解码器(byte -> MoResponse)
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
public class MoResponseDecoder extends ByteToMessageDecoder {

    private static final int BASE_LENGTH = 4 + 2 + 2 + 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        System.out.println("MoResponseDecoder 解码开始");

        // 循环至数据包不足
        while (byteBuf.readableBytes() >= BASE_LENGTH){

            // 寻找包头
            while (true){
                byteBuf.markReaderIndex();
                int val = byteBuf.readInt();
                if(val == CodecConst.PROTOCOL_FLAG){
                    break;
                }
                byteBuf.resetReaderIndex();
                byteBuf.readByte();

                if(byteBuf.readableBytes() < BASE_LENGTH){
                    return;
                }
            }

            MoResponse response = new MoResponse();
            response.setModule(byteBuf.readShort());
            response.setCmd(byteBuf.readShort());
            response.setStatusCode(byteBuf.readShort());
            response.setLength(byteBuf.readInt());
            if(response.getLength() > 0){
                byte[] data = new byte[response.getLength()];
                byteBuf.readBytes(data);
                response.setData(data);
            }

            list.add(response);
        }

    }

}
