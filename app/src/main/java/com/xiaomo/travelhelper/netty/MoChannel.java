package com.xiaomo.travelhelper.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;

/**
 * author: mojiale66@163.com
 * date:   2018/4/27
 * description:  基于代理模式重写通道
 */
public class MoChannel extends MoChannelWrapper {

    public MoChannel(Channel channel) {
        super(channel);
    }

    @Override
    public ChannelFuture write(Object o) {
        return super.write(changeToMoRequestOrMoResponse(o));
    }

    @Override
    public ChannelFuture write(Object o, ChannelPromise channelPromise) {
        return super.write(changeToMoRequestOrMoResponse(o), channelPromise);
    }


    @Override
    public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
        return super.writeAndFlush(changeToMoRequestOrMoResponse(o), channelPromise);
    }

    @Override
    public ChannelFuture writeAndFlush(Object o) {
        return super.writeAndFlush(changeToMoRequestOrMoResponse(o));
    }

    private Object changeToMoRequestOrMoResponse(Object o){

        if(o instanceof AbstractRequestSerializer){
            AbstractRequestSerializer obj = (AbstractRequestSerializer) o;
            MoRequest request = new MoRequest();
            request.setModule(obj.getModule());
            request.setCmd(obj.getCmd());
            byte[] data = obj.getBytes();
            if(data != null && data.length > 0){
                request.setLength(data.length);
                request.setData(data);
            }
            return request;
        }

        if(o instanceof AbstractResponseSerializer){
            AbstractResponseSerializer obj = (AbstractResponseSerializer) o;
            MoResponse response = new MoResponse();
            response.setModule(obj.getModule());
            response.setCmd(obj.getCmd());
            response.setStatusCode(obj.getStatusCode());
            byte[] data = obj.getBytes();
            if(data != null && data.length > 0){
                response.setLength(data.length);
                response.setData(data);
            }
            return response;
        }

        return o;
    }





}
