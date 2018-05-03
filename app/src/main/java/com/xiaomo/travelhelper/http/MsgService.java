package com.xiaomo.travelhelper.http;

import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.MsgFriendResult;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 消息推送服务类
 */

public interface MsgService {

    //String BASE_URL = "http://192.168.1.136:8080/api/msg/";
    String BASE_URL = UserConst.BASE_URL + "api/msg/";

    @POST("listMsgFriend")
    Observable<MsgFriendResult> listMsgFriend(@Query("account") String account, @Query("token") String token);

}
