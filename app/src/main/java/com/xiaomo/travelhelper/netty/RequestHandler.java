package com.xiaomo.travelhelper.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 请求处理器
 */
public class RequestHandler {

    private Session session;
    private MoChannel moChannel;
    private MoRequest moRequest;
    private ChannelHandlerContext channelHandlerContext;


    public RequestHandler(MoChannel moChannel, MoRequest moRequest, ChannelHandlerContext channelHandlerContext) {
        this.moChannel = moChannel;
        this.moRequest = moRequest;
        this.channelHandlerContext = channelHandlerContext;
        session = new SessionImpl(moChannel);
    }

    public void write(Object message){
        if(session != null){
            session.write(message);
        }
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

    public MoRequest getMoRequest() {
        return moRequest;
    }

    public void setMoRequest(MoRequest moRequest) {
        this.moRequest = moRequest;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
