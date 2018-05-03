package com.xiaomo.travelhelper.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 客户端启动类
 */
public class MoClientStart {

    /**服务机IP*/
    private String host;
    /**服务及端口*/
    private int port;
    /**服务类*/
    private Bootstrap bootstrap = new Bootstrap();
    /**通道*/
    private Channel channel;
    private MoChannel moChannel;
    /**线程池*/
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private List<ChannelHandler> channelHandlerList;

    public MoClientStart(String host,int port,List<ChannelHandler> channelHandlerList){
        this.host = host;
        this.port = port;
        this.channelHandlerList = channelHandlerList;
        init();
    }

    private void init(){
        // 设置循环线程组事例
        bootstrap.group(workerGroup);
        // 设置channel工厂
        bootstrap.channel(NioSocketChannel.class);
        // 设置管道
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new MoResponseDecoder());
                ch.pipeline().addLast(new MoRequestEncoder());
                ch.pipeline().addLast(new MoClientHandler());
                /*if(channelHandlerList != null){
                    for(ChannelHandler channelHandler : channelHandlerList){
                        ch.pipeline().addLast(channelHandler);
                    }
                }*/
            }
        });

    }

    public void connect() throws InterruptedException{
        System.out.println("连接客户端-host:" + host + ",port:" + port);
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(host, port));
        connect.sync();
        channel = connect.channel();
        moChannel = new MoChannel(channel);

    }

    public void shutdown() {
        channel = null;
        moChannel = null;
        workerGroup.shutdownGracefully();
    }

    public MoChannel getChannel() {
        return moChannel;
    }

    public void send(Object message){
        if(moChannel == null || !moChannel.isActive()){
            try {
                connect();
            }catch (InterruptedException e){
                System.out.println("连接失败，失败原因-"+e.getMessage());
            }

        }
        moChannel.writeAndFlush(message);
    }





}
