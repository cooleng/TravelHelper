package com.xiaomo.travelhelper.netty;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 抽象序列化器
 */
public abstract class AbstractSerializer {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    protected ByteBuf writeBuffer;

    protected ByteBuf readBuffer;

    /**
     * 反序列化具体实现
     */
    protected abstract void read();

    /**
     * 序列化具体实现
     */
    protected abstract void write();

    /**
     * byte -> AbstractSerializer
     * @param bytes	字节数组
     */
    public AbstractSerializer readFromBytes(byte[] bytes) {
        readBuffer = BufferFactory.getBuffer(bytes);
        read();
        readBuffer.clear();
        ReferenceCountUtil.release(readBuffer);
        return this;
    }

    /**
     * ByteBuf -> AbstractSerializer
     * @param readBuffer
     */
    public AbstractSerializer readFromBuffer(ByteBuf readBuffer) {
        this.readBuffer = readBuffer;
        read();
        return this;
    }

    /**
     * AbstractSerializer -> ByteBuf
     * @return
     */
    public ByteBuf writeToLocalBuff(){
        writeBuffer = BufferFactory.getBuffer();
        write();
        return writeBuffer;
    }

    /**
     * AbstractSerializer -> ByteBuf
     * @param buffer 写入的目标 ByteBuf
     * @return
     */
    public ByteBuf writeToTargetBuff(ByteBuf buffer){
        writeBuffer = buffer;
        write();
        return writeBuffer;
    }

    /**
     * AbstractSerializer -> byte
     *
     * @return
     */
    public byte[] getBytes() {
        writeToLocalBuff();
        byte[] bytes;
        if (writeBuffer.writerIndex() == 0) {
            bytes = new byte[0];
        } else {
            bytes = new byte[writeBuffer.writerIndex()];
            writeBuffer.readBytes(bytes);
        }
        writeBuffer.clear();
        ReferenceCountUtil.release(writeBuffer);
        return bytes;
    }


    public byte readByte() {
        return readBuffer.readByte();
    }

    public short readShort() {
        return readBuffer.readShort();
    }

    public int readInt() {
        return readBuffer.readInt();
    }

    public long readLong() {
        return readBuffer.readLong();
    }

    public float readFloat() {
        return readBuffer.readFloat();
    }

    public double readDouble() {
        return readBuffer.readDouble();
    }

    public String readString() {
        int size = readBuffer.readShort();
        if (size <= 0) {
            return "";
        }

        byte[] bytes = new byte[size];
        readBuffer.readBytes(bytes);

        return new String(bytes, CHARSET);
    }

    public <T> List<T> readList(Class<T> clz) {
        List<T> list = Lists.newArrayList();
        int size = readBuffer.readShort();
        for (int i = 0; i < size; i++) {
            list.add(read(clz));
        }
        return list;
    }

    public <K,V> Map<K,V> readMap(Class<K> keyClz, Class<V> valueClz) {
        Map<K,V> map = Maps.newHashMap();
        int size = readBuffer.readShort();
        for (int i = 0; i < size; i++) {
            K key = read(keyClz);
            V value = read(valueClz);
            map.put(key, value);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public <I> I read(Class<I> clz) {
        Object t = null;
        if ( clz == int.class || clz == Integer.class) {
            t = this.readInt();
        } else if (clz == byte.class || clz == Byte.class){
            t = this.readByte();
        } else if (clz == short.class || clz == Short.class){
            t = this.readShort();
        } else if (clz == long.class || clz == Long.class){
            t = this.readLong();
        } else if (clz == float.class || clz == Float.class){
            t = readFloat();
        } else if (clz == double.class || clz == Double.class){
            t = readDouble();
        } else if (clz == String.class ){
            t = readString();
        } else if (AbstractSerializer.class.isAssignableFrom(clz)){
            try {
                byte hasObject = this.readBuffer.readByte();
                if(hasObject == 1){
                    AbstractSerializer temp = (AbstractSerializer)clz.newInstance();
                    temp.readFromBuffer(this.readBuffer);
                    t = temp;
                }else{
                    t = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            throw new RuntimeException(String.format("不支持类型:[%s]", clz));
        }
        return (I) t;
    }


    public AbstractSerializer writeByte(Byte value) {
        writeBuffer.writeByte(value);
        return this;
    }

    public AbstractSerializer writeShort(Short value) {
        writeBuffer.writeShort(value);
        return this;
    }

    public AbstractSerializer writeInt(Integer value) {
        writeBuffer.writeInt(value);
        return this;
    }

    public AbstractSerializer writeLong(Long value) {
        writeBuffer.writeLong(value);
        return this;
    }

    public AbstractSerializer writeFloat(Float value) {
        writeBuffer.writeFloat(value);
        return this;
    }

    public AbstractSerializer writeDouble(Double value) {
        writeBuffer.writeDouble(value);
        return this;
    }

    public <T> AbstractSerializer writeList(List<T> list) {
        if (isEmpty(list)) {
            writeBuffer.writeShort((short) 0);
            return this;
        }
        writeBuffer.writeShort((short) list.size());
        for (T item : list) {
            writeObject(item);
        }
        return this;
    }

    public <K,V> AbstractSerializer writeMap(Map<K, V> map) {
        if (isEmpty(map)) {
            writeBuffer.writeShort((short) 0);
            return this;
        }
        writeBuffer.writeShort((short) map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            writeObject(entry.getKey());
            writeObject(entry.getValue());
        }
        return this;
    }

    public AbstractSerializer writeString(String value) {
        if (value == null || value.isEmpty()) {
            writeShort((short) 0);
            return this;
        }

        byte data[] = value.getBytes(CHARSET);
        short len = (short) data.length;
        writeBuffer.writeShort(len);
        writeBuffer.writeBytes(data);
        return this;
    }

    public AbstractSerializer writeObject(Object object) {

        if(object == null){
            writeByte((byte)0);
        }else{
            if (object instanceof Integer) {
                writeInt((Integer) object);
                return this;
            }

            if (object instanceof Long) {
                writeLong((Long) object);
                return this;
            }

            if (object instanceof Short) {
                writeShort((Short) object);
                return this;
            }

            if (object instanceof Byte) {
                writeByte((Byte) object);
                return this;
            }

            if (object instanceof String) {
                String value = (String) object;
                writeString(value);
                return this;
            }
            if (object instanceof AbstractSerializer) {
                writeByte((byte)1);
                AbstractSerializer value = (AbstractSerializer) object;
                value.writeToTargetBuff(writeBuffer);
                return this;
            }

            throw new RuntimeException("不可序列化的类型:" + object.getClass());
        }

        return this;
    }

    private <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }
    public <K,V> boolean isEmpty(Map<K,V> c) {
        return c == null || c.size() == 0;
    }


}
