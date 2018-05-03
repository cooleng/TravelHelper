package com.xiaomo.travelhelper.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 请求解码器(byte -> MoRequest)
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号  | 命令号 | 长度   |  数据    |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * 包头4字节
 * 模块号2字节
 * 命令号2字节
 * 数据长度4字节
 */
public class MoRequestDecoder extends ByteToMessageDecoder{

    /**数据包基本长度*/
    private static final int BASE_LENGTH = 4 + 2 + 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        System.out.println("MoRequestDecoder 解析开始");

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

            MoRequest request = new MoRequest();
            request.setModule(byteBuf.readShort());
            request.setCmd(byteBuf.readShort());
            request.setLength(byteBuf.readInt());
            if(request.getLength() > 0){
                byte[] data = new byte[request.getLength()];
                byteBuf.readBytes(data);
                request.setData(data);
            }

            list.add(request);
        }

    }



}
