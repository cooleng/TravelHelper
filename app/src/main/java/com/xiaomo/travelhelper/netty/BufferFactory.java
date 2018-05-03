package com.xiaomo.travelhelper.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.ByteOrder;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: byteBuf 工厂类
 */
public class BufferFactory {

    private static ByteBufAllocator bufAllocator = PooledByteBufAllocator.DEFAULT;
    private static ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    @SuppressWarnings("deprecation")
    public static ByteBuf getBuffer() {
        ByteBuf buffer = bufAllocator.heapBuffer();
        buffer = buffer.order(BYTE_ORDER);
        return buffer;
    }


    @SuppressWarnings("deprecation")
    public static ByteBuf getBuffer(byte[] bytes) {
        ByteBuf buffer = bufAllocator.heapBuffer();
        buffer = buffer.order(BYTE_ORDER);
        buffer.writeBytes(bytes);
        return buffer;
    }

}
