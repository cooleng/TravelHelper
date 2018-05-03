package com.xiaomo.travelhelper.http;

import android.util.Log;

import com.xiaomo.travelhelper.model.chat.OnLineDTO;
import com.xiaomo.travelhelper.netty.MoClientStart;

/**
 * 聊天服务类
 */

public class ChatService {

    private static MoClientStart client = new MoClientStart("192.168.19.22",8888,null);

    public static void online(String account,String username){

        Log.i("ChatService",account + "-" + username + "上线连接");
        OnLineDTO onLineDTO = new OnLineDTO();
        onLineDTO.setAccount(account);
        onLineDTO.setUsername(username);
        onLineDTO.setModule((short)1);
        onLineDTO.setCmd((short)3);
        if(client == null){
            client = new MoClientStart("192.168.19.22",8888,null);
        }
        client.send(onLineDTO);

    }

    public static void send(Object o){

        Log.i("ChatService","发送对象-" + o);

        if(client == null){
            client = new MoClientStart("192.168.19.22",8888,null);
        }
        client.send(o);
    }

    public static void disconnect(){

        Log.i("ChatService","断开连接-client.shutdown()");
        client.shutdown();
        client = null;
    }


}
