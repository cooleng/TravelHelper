package com.xiaomo.travelhelper.netty;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description: 请求实体抽象序列化器
 */
public abstract class AbstractRequestSerializer extends AbstractSerializer{

    private short module;
    private short cmd;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


}
