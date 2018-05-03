package com.xiaomo.travelhelper.http;

import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.circle.ShareResult;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 分享服务类接口
 */

public interface ShareService {

    //String BASE_URL = "http://192.168.1.136:8080/api/share/";
    String BASE_URL = UserConst.BASE_URL + "api/share/";

    @POST("send")
    Observable<BaseResult> send(@Query("account") String account, @Query("token") String token
            ,@Query("content") String content,@Query("imgUrl") String imgUrl);


    @POST("list")
    Observable<ShareResult> listShare(@Query("account") String account, @Query("token") String token);

    @POST("listByPage")
    Observable<ShareResult> listByPage(@Query("account") String account, @Query("token") String token
            , @Query("pageSize") Integer pageSize, @Query("pageNo") Integer pageNo);

    @POST("comment")
    Observable<BaseResult> comment(@Query("account") String account, @Query("token") String token
            , @Query("shareId") Integer shareId, @Query("commentorId") String commentorId
            ,@Query("content") String content);

}
