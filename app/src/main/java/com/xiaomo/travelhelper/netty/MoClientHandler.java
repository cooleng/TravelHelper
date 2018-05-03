package com.xiaomo.travelhelper.netty;


import android.util.Log;

import com.xiaomo.travelhelper.model.chat.PrivateChatModel;
import com.xiaomo.travelhelper.model.chat.PrivateChatResponseDTO;

import java.util.Date;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 客户端处理实现类
 */
public class MoClientHandler extends AbstractMoClientHandler{

    @Override
    public void channelRead0(MoChannel moChannel, MoResponse moResponse, ChannelHandlerContext channelHandlerContext) throws Exception {
        RequestHandler requestHandler = new RequestHandler(moChannel,null,channelHandlerContext);
        ResponseHandler responseHandler = new ResponseHandler(moChannel,moResponse,channelHandlerContext);
        service(requestHandler,responseHandler);
    }

    @Override
    public void channelInactive(MoChannel moChannel, ChannelHandlerContext ctx) throws Exception {

        RequestHandler requestHandler = new RequestHandler(moChannel,null,ctx);
        ResponseHandler responseHandler = new ResponseHandler(moChannel,null,ctx);
        inactive(requestHandler,responseHandler);
    }

    /**
     * 收到响应信息处理
     * @param requestHandler
     * @param responseHandler
     */
    public void service(RequestHandler requestHandler,ResponseHandler responseHandler){


        MoResponse response = responseHandler.getMoResponse();
        if(response != null){

            PrivateChatResponseDTO responseDTO = new PrivateChatResponseDTO();
            responseDTO = (PrivateChatResponseDTO) responseDTO.readFromBytes(response.getData());
            Log.i("ChatService","收到响应-" + responseDTO);
            PrivateChatModel model = new PrivateChatModel();
            model.setTime(new Date(System.currentTimeMillis()));
            model.setFromAccount(responseDTO.getFromAccount());
            model.setFromUsername(responseDTO.getFromUsername());
            model.setToUsername(responseDTO.getToUsername());
            model.setToAccount(responseDTO.getToAccount());
            model.setContent(responseDTO.getContent());
            model.setFromImg(responseDTO.getFromImg());
            model.setToImg(responseDTO.getToImg());
            model.setFlag(2);
            model.save();

        }else {
            System.out.println("找不到请求体，请求异常无法处理");
        }
    }

    /**
     * 断开链接处理
     * @param requestHandler
     * @param responseHandler
     */
    public void inactive(RequestHandler requestHandler,ResponseHandler responseHandler){

        responseHandler.getMoChannel().disconnect();
        System.out.println("断开连接");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

        Channel channel = ctx.channel();
        System.out.println("MoClientHandler 捕获异常 - " + cause.getMessage());
        cause.printStackTrace();
        if(channel.isActive()){
            ctx.close();
        }

    }



}
