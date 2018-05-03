package com.xiaomo.travelhelper.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 客户端抽象处理器
 */
public abstract class AbstractMoClientHandler extends SimpleChannelInboundHandler<MoResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MoResponse moResponse) throws Exception {

        System.out.println("接收到响应：AbstractMoClientHandler - " + moResponse);

        Channel channel = channelHandlerContext.channel();
        MoChannel moChannel = new MoChannel(channel);
        channelRead0(moChannel,moResponse,channelHandlerContext);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        MoChannel moChannel = new MoChannel(channel);
        channelInactive(moChannel,ctx);
    }

    public abstract void channelRead0(MoChannel moChannel,MoResponse moResponse,ChannelHandlerContext channelHandlerContext) throws Exception;

    public abstract void channelInactive(MoChannel moChannel,ChannelHandlerContext ctx) throws Exception;


}
