package com.xiaomo.travelhelper.netty;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 响应实体抽象序列化器
 */
public abstract class AbstractResponseSerializer extends AbstractSerializer{

    private short module;
    private short cmd;
    private short statusCode;
    private int length;

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

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
