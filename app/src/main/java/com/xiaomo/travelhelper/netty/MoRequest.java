package com.xiaomo.travelhelper.netty;

import java.util.Arrays;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 请求实体基类
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号  | 命令号 | 长度   |  数据    |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * 包头4字节
 * 模块号2字节
 * 命令号2字节
 * 数据长度4字节
 */
public class MoRequest {

    /**模块号*/
    private short module;

    /**命令号*/
    private short cmd;

    /**数据报文长度*/
    private int length;

    /**数据报文*/
    private byte[] data;

    public short getModule() {
        return module;
    }

    public void setModule(short module) {
        this.module = module;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MoRequest{" +
                "module=" + module +
                ", cmd=" + cmd +
                ", length=" + length +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
