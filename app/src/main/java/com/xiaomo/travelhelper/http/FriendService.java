package com.xiaomo.travelhelper.http;


import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.FriendResult;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 朋友服务类
 */

public interface FriendService {

    //String BASE_URL = "http://192.168.1.136:8080/api/friend/";
    String BASE_URL = UserConst.BASE_URL + "api/friend/";

    @POST("list")
    Observable<FriendResult> listFriends(@Query("account") String account, @Query("token") String token);

    @POST("addFriend")
    Observable<BaseResult> addFriend(@Query("account") String account, @Query("token") String token
            ,@Query("targetAccount") String targetAccount,@Query("desc") String desc);

    @POST("agreeFriend")
    Observable<BaseResult> agreeFriend(@Query("account") String account, @Query("token") String token
            , @Query("targetAccount") String targetAccount);

    @POST("likeByAccountOrUsername")
    Observable<FriendResult> searchFriend(@Query("account") String account, @Query("token") String token
            ,@Query("val") String val);


}
