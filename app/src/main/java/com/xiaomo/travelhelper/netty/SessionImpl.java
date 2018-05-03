package com.xiaomo.travelhelper.netty;

import io.netty.util.AttributeKey;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 会话管理实现
 */
public class SessionImpl implements Session {

    private MoChannel moChannel;

    /**绑定对象key*/
    private static AttributeKey<Object> ATTACHMENT_KEY  = AttributeKey.valueOf("ATTACHMENT_KEY");

    public SessionImpl(MoChannel moChannel){
        this.moChannel = moChannel;
    }

    @Override
    public Object getAttachment() {
        return moChannel.attr(ATTACHMENT_KEY).get();
    }

    @Override
    public void setAttachment(Object attachment) {
        moChannel.attr(ATTACHMENT_KEY).set(attachment);
    }

    @Override
    public void removeAttachment() {
        moChannel.attr(ATTACHMENT_KEY).remove();
    }

    @Override
    public void write(Object message) {
        moChannel.writeAndFlush(message);
    }

    @Override
    public boolean isConnected() {
        return moChannel.isActive();
    }

    @Override
    public void close() {
        moChannel.close();
    }

    public MoChannel getMoChannel() {
        return moChannel;
    }

    public void setMoChannel(MoChannel moChannel) {
        this.moChannel = moChannel;
    }

}
