package com.xiaomo.travelhelper.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 响应处理器
 */
public class ResponseHandler {

    private Session session;
    private MoChannel moChannel;
    private MoResponse moResponse;
    private ChannelHandlerContext channelHandlerContext;

    public ResponseHandler(MoChannel moChannel,MoResponse moResponse, ChannelHandlerContext channelHandlerContext) {
        this.moChannel = moChannel;
        this.moResponse = moResponse;
        this.channelHandlerContext = channelHandlerContext;
        session = new SessionImpl(moChannel);
    }

    public void write(Object message){
        if(session != null){
            session.write(message);
        }
    }

    public MoResponse getMoResponse() {
        return moResponse;
    }

    public void setMoResponse(MoResponse moResponse) {
        this.moResponse = moResponse;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MoChannel getMoChannel() {
        return moChannel;
    }

    public void setMoChannel(MoChannel moChannel) {
        this.moChannel = moChannel;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

}
