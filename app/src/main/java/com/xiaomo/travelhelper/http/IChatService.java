package com.xiaomo.travelhelper.http;

import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.PrivateChatResult;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 聊天服务接口
 */

public interface IChatService {

    String BASE_URL = UserConst.BASE_URL + "api/chat/";

    @POST("list")
    Observable<PrivateChatResult> listMsg(@Query("account") String account, @Query("token") String token);

}
